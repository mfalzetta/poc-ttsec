/*
cm: common
[10:47:48] Diana Muñoz: ct: control de acceso
[10:47:52] Diana Muñoz: at: atsys
[10:47:55] Diana Muñoz: em: email
*/


/**********************************************************/
/*************************** AT ***************************/
/**********************************************************/
-- Categorias
SELECT * FROM at_category;            -- Para que serve o campo CM_Status?
SELECT * FROM at_category_data;
SELECT * FROM at_category_at_actions;

-- AS Number
SELECT * FROM AT_AUT_SYS_CATEGORY;    -- Mapeamento de AS
SELECT * FROM AT_AUTONOMOUS_SYSTEM;   -- Mapeamento de AS

SELECT * FROM at_action;              -- Para que serve essa tabela?

--  Reclamação
SELECT * FROM AT_INTERACTION;         -- Registro de atividades realizadas com a reclamação
SELECT * FROM AT_INTERACTION_HISTORY;
SELECT * FROM AT_INTERACTION_NOTE;
/**********************************************************/
/************************* FIM AT *************************/
/**********************************************************/

select * from cm_message;
select * from cm_msg_category;
SELECT * FROM CM_SERVICE_OPERATOR ;  
select * from ct_application;


-- Usuários web	
select * from ct_user;	
select * from ct_user_role;	
select * from ct_role;	
select * from ct_role_option;	
select * from CT_ROLE_OPTION_CATEGORY;	
