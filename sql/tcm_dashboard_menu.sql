-- Patch: add TCM Dashboard (Workbench) menu for existing databases already on tcm_mvp.sql without this entry.
-- UTF-8. Run against your RuoYi DB (e.g. ry-vue).

insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select 'Dashboard', m.menu_id, 0, 'dashboard', 'tcm/dashboard/index', '', '', 1, 0, 'C', '0', '0', 'biz:base:list', 'chart', 'admin', sysdate(), 'TCM enterprise dashboard'
from sys_menu m where m.path = 'tcm' and m.menu_type = 'M'
and not exists (select 1 from sys_menu c where c.path = 'dashboard' and c.component = 'tcm/dashboard/index');

insert into sys_role_menu(role_id, menu_id)
select role.role_id, menu.menu_id
from sys_role role
join sys_menu menu on menu.path = 'dashboard' and menu.component = 'tcm/dashboard/index'
where role.role_key = 'tcm_enterprise'
  and not exists (select 1 from sys_role_menu rm where rm.role_id = role.role_id and rm.menu_id = menu.menu_id);
