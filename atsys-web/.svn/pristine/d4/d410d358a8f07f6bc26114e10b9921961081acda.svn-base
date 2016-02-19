package com.sixlabs.atsys.web.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.*;

public class SimpleGroup extends SimplePrincipal implements Group {

    private Map<Principal, Principal> members;

    public SimpleGroup(String groupName) {
        super(groupName);
        members = new HashMap<Principal, Principal>(3);
    }

    /**
     * Adds the specified member to the group.
     *
     * @param user the principal to add to this group.
     * @return true if the member was successfully added,
     * false if the principal was already a member.
     */
    public boolean addMember(Principal user) {
        boolean isMember = members.containsKey(user);
        if (!isMember) members.put(user, user);
        return !isMember;
    }

    /**
     * Returns true if the passed principal is a member of the group.
     * This method does a recursive search, so if a principal belongs to a
     * group which is a member of this group, true is returned.
     * <p/>
     * A special check is made to see if the member is an instance of
     * com.sixlabs.atsys.web.security.AnybodyPrincipal or com.sixlabs.atsys.web.security.NobodyPrincipal
     * since these classes do not hash to meaningful values.
     *
     * @param member the principal whose membership is to be checked.
     * @return true if the principal is a member of this group,
     * false otherwise.
     */
    public boolean isMember(Principal member) {
        // First see if there is a key with the member name
        boolean isMember = members.containsKey(member);
        if (!isMember) {   // Check the AnybodyPrincipal & NobodyPrincipal special cases
            isMember = (member instanceof AnybodyPrincipal);
            if (!isMember) {
                if (member instanceof NobodyPrincipal)
                    return false;
            }
        }
        if (!isMember) {   // Check any Groups for membership
            Collection values = members.values();
            Iterator iter = values.iterator();
            while (!isMember && iter.hasNext()) {
                Object next = iter.next();
                if (next instanceof Group) {
                    Group group = (Group) next;
                    isMember = group.isMember(member);
                }
            }
        }
        return isMember;
    }

    /**
     * Returns an enumeration of the members in the group.
     * The returned objects can be instances of either Principal
     * or Group (which is a subinterface of Principal).
     *
     * @return an enumeration of the group members.
     */
    public Enumeration<? extends Principal> members() {
        return Collections.enumeration(members.values());
    }

    /**
     * Removes the specified member from the group.
     *
     * @param user the principal to remove from this group.
     * @return true if the principal was removed, or
     * false if the principal was not a member.
     */
    public boolean removeMember(Principal user) {
        Object prev = members.remove(user);
        return prev != null;
    }

    public String toString() {
        StringBuilder tmp = new StringBuilder(getName());
        tmp.append("(members:");
        for (Object o : members.keySet()) {
            tmp.append(o);
            tmp.append(',');
        }
        tmp.setCharAt(tmp.length() - 1, ')');
        return tmp.toString();
    }
}
