package com.sixlabs.atsys.web.jsf.listener;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.*;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

/**
 * Use this action listener when you want to partially (ajax) render input fields which are not executed during submit,
 * but which are possibly in an invalidated state because of a validation failure during a previous request. Those
 * input fields will be resetted so that they are not in an invalidated state anymore.
 * <p>
 * How does it work? First, here are some JSF facts:
 * <ul>
 * <li>When JSF validation succeeds for a particular input component during the validations phase, then the submitted
 * value is set to <code>null</code> and the validated value is set as local value of the input component.
 * <li>When JSF validation fails for a particular input component during the validations phase, then the submitted
 * value is kept in the input component.
 * <li>When at least one input component is invalid after the validations phase, then JSF will not update the model
 * values for any of the input components. JSF will directly proceed to render response phase.
 * <li>When JSF renders input components, then it will first test if the submitted value is not <code>null</code> and
 * then display it, else if the local value is not <code>null</code> and then display it, else it will display the
 * model value.
 * <li>As long as you're interacting with the same JSF view, you're dealing with the same component state.
 * </ul>
 * <p>
 * So, when the validation has failed for a particular form submit and you happen to need to update the values of input
 * fields by a different ajax action or even a different ajax form (e.g. populating a field depending on a dropdown
 * selection or the result of some modal dialog form, etc), then you basically need to reset the target input
 * components in order to get JSF to display the model value which was edited during invoke action. Otherwise JSF will
 * still display its local value as it was during the validation failure and keep them in an invalidated state.
 * <p>
 * The {@link ResetInputAjaxActionListener} is designed to solve exactly this problem. There are basically three ways
 * to configure and use it:
 * <ul>
 * <li><p>Register it as <code>&lt;phase-listener&gt;</code> in <code>faces-config.xml</code>. It'll be applied
 * to <strong>every single</strong> ajax action throughout the webapp, on both <code>UIInput</code> and
 * <code>UICommand</code> components.
 * <pre>
 * &lt;lifecycle&gt;
 *   &lt;phase-listener&gt;org.omnifaces.eventlistener.ResetInputAjaxActionListener&lt;/phase-listener&gt;
 * &lt;/lifecycle&gt;
 * </pre>
 * <li><p><i>Or</i> register it as <code>&lt;action-listener&gt;</code> in <code>faces-config.xml</code>. It'll
 * <strong>only</strong> be applied to ajax actions which are invoked by an <code>UICommand</code> component such as
 * <code>&lt;h:commandButton&gt;</code> and <code>&lt;h:commandLink&gt;</code>.
 * <pre>
 * &lt;application&gt;
 *   &lt;action-listener&gt;org.omnifaces.eventlistener.ResetInputAjaxActionListener&lt;/action-listener&gt;
 * &lt;/application&gt;
 * </pre>
 * <li><p><i>Or</i> register it as <code>&lt;f:actionListener&gt;</code> on the invidivual <code>UICommand</code>
 * components where this action listener is absolutely necessary to solve the concrete problem. Note that it isn't
 * possible to register it on the individual <code>UIInput</code> components using the standard JSF tags.
 * <pre>
 * &lt;h:commandButton value="Update" action="#{bean.updateOtherInputs}"&gt;
 *   &lt;f:ajax execute="currentInputs" render="otherInputs" /&gt;
 *   &lt;f:actionListener type="org.omnifaces.eventlistener.ResetInputAjaxActionListener" /&gt;
 * &lt;/h:commandButton&gt;
 * </pre>
 * </ul>
 * <p>
 * This works with standard JSF, PrimeFaces and RichFaces actions. Only for RichFaces there's a reflection hack,
 * because its <code>ExtendedPartialViewContextImpl</code> <i>always</i> returns an empty collection for render IDs.
 * See also <a href="https://issues.jboss.org/browse/RF-11112">RF issue 11112</a>.
 * <p>
 * Design notice: being a phase listener was mandatory in order to be able to hook on every single ajax action as
 * standard JSF API does not (seem to?) offer any ways to register some kind of {@link AjaxBehaviorListener} in an
 * application wide basis, let alone on a per <code>&lt;f:ajax&gt;</code> tag basis, so that it also get applied to
 * ajax actions in <code>UIInput</code> components. There are ways with help of {@link SystemEventListener}, but it
 * ended up to be too clumsy.
 *
 * @author Bauke Scholtz
 * @link http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-1060
 */
public class ResetInputAjaxActionListener extends DefaultPhaseListener implements ActionListener {

    // Constants ------------------------------------------------------------------------------------------------------

    private static final long serialVersionUID = -5317382021715077662L;

    private static final Set<VisitHint> VISIT_HINTS = EnumSet.of(VisitHint.SKIP_TRANSIENT, VisitHint.SKIP_UNRENDERED);

    // Variables ------------------------------------------------------------------------------------------------------

    private ActionListener wrapped;

    // Constructors ---------------------------------------------------------------------------------------------------

    /**
     * Construct a new reset input ajax action listener. This constructor will be used when specifying the action
     * listener by <code>&lt;f:actionListener&gt;</code> or when registering as <code>&lt;phase-listener&gt;</code> in
     * <code>faces-config.xml</code>.
     */
    public ResetInputAjaxActionListener() {
        this(null);
    }

    /**
     * Construct a new reset input ajax action listener around the given wrapped action listener. This constructor
     * will be used when registering as <code>&lt;action-listener&gt;</code> in <code>faces-config.xml</code>.
     *
     * @param wrapped The wrapped action listener.
     */
    public ResetInputAjaxActionListener(ActionListener wrapped) {
        super(PhaseId.INVOKE_APPLICATION);
        this.wrapped = wrapped;
    }

    // Actions --------------------------------------------------------------------------------------------------------

    /**
     * Delegate to the {@link #processAction(ActionEvent)} method when this action listener is been registered as a
     * phase listener so that it get applied on <strong>all</strong> ajax requests.
     *
     * @see #processAction(ActionEvent)
     */
    @Override
    public void beforePhase(PhaseEvent event) {
        processAction(null);
    }

    /**
     * Handle the reset input action as follows, only and only if the current request is an ajax request and the
     * {@link PartialViewContext#getRenderIds()} does not return an empty collection nor is the same as
     * {@link PartialViewContext#getExecuteIds()}: find all {@link EditableValueHolder} components based on
     * {@link PartialViewContext#getRenderIds()} and if the component is not covered by
     * {@link PartialViewContext#getExecuteIds()}, then invoke {@link EditableValueHolder#resetValue()} on the
     * component.
     *
     * @throws IllegalArgumentException When one of the client IDs resolved to a <code>null</code> component. This
     *                                  would however indicate a bug in the concrete {@link PartialViewContext} implementation which is been used.
     */
    @Override
    public void processAction(ActionEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        PartialViewContext partialViewContext = context.getPartialViewContext();

        if (partialViewContext.isAjaxRequest()) {
            Collection<String> renderIds = getRenderIds(partialViewContext);
            Collection<String> executeIds = partialViewContext.getExecuteIds();

            if (!renderIds.isEmpty() && !renderIds.containsAll(executeIds)) {
                resetEditableValueHolders(VisitContext.createVisitContext(
                        context, renderIds, VISIT_HINTS), context.getViewRoot(), executeIds);
            }
        }

        if (wrapped != null && event != null) {
            wrapped.processAction(event);
        }
    }

    // Helpers --------------------------------------------------------------------------------------------------------

    /**
     * Helper method with RichFaces4 hack to return the proper render IDs from the given partial view context.
     *
     * @param partialViewContext The partial view context to return the render IDs for.
     * @return The render IDs.
     */
    private static Collection<String> getRenderIds(PartialViewContext partialViewContext) {
        return partialViewContext.getRenderIds();
    }

    /**
     * Find all editable value holder components in the component hierarchy, starting with the given component and
     * reset them when they are not covered by the given execute IDs.
     *
     * @param context    The visit context to work with.
     * @param component  The starting point of the component hierarchy to look for editable value holder components.
     * @param executeIds The execute IDs.
     */
    private void resetEditableValueHolders(VisitContext context, final UIComponent component, final Collection<String> executeIds) {
        component.visitTree(context, (context1, target) -> {

            if (executeIds.contains(target.getClientId(context1.getFacesContext()))) {
                return VisitResult.REJECT;
            }
            if (target instanceof EditableValueHolder) {
                ((EditableValueHolder) target).resetValue();
            } else if (context1.getIdsToVisit() != VisitContext.ALL_IDS) {
                // Render ID didn't point an EditableValueHolder. Visit all children as well.
                resetEditableValueHolders(VisitContext.createVisitContext(
                        context1.getFacesContext(), null, context1.getHints()), target, executeIds);
            }
            return VisitResult.ACCEPT;
        });
    }

}
