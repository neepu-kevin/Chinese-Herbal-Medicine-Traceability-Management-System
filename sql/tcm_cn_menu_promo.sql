-- Patch: rename TCM menus to Chinese + add Herb Promotion page/menu.
-- UTF-8. Run against your RuoYi DB (e.g. ry-vue).

-- ----------------------------
-- 1) Rename existing menus (English -> Chinese)
-- ----------------------------
update sys_menu set menu_name = '�в�ҩ��Դ' where path = 'tcm' and menu_type = 'M';
update sys_menu set menu_name = '����̨' where component = 'tcm/dashboard/index';
update sys_menu set menu_name = '��ֲ����' where component = 'tcm/base/index';
update sys_menu set menu_name = '��Ʒ����' where component = 'tcm/product/index';
update sys_menu set menu_name = '��������' where component = 'tcm/batch/index';
update sys_menu set menu_name = '���ڼ�¼' where component = 'tcm/process/index';
update sys_menu set menu_name = '��Դ���ѯ' where component = 'tcm/trace/index';
update sys_menu set menu_name = '����������' where component = 'tcm/review/index';

-- ----------------------------
-- 2) Add new menu: Herb Promotion (enterprise)
-- ----------------------------
insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select '��ҩ����', m.menu_id, 7, 'promo', 'tcm/promo/index', '', '', 1, 0, 'C', '0', '0', 'biz:promo:list', 'shopping', 'admin', sysdate(), 'Herb promotion page'
from sys_menu m
where m.path = 'tcm' and m.menu_type = 'M'
  and not exists (select 1 from sys_menu c where c.path = 'promo' and c.component = 'tcm/promo/index');

-- bind to enterprise role
insert into sys_role_menu(role_id, menu_id)
select role.role_id, menu.menu_id
from sys_role role
join sys_menu menu on menu.path = 'promo' and menu.component = 'tcm/promo/index'
where role.role_key = 'tcm_enterprise'
  and not exists (select 1 from sys_role_menu rm where rm.role_id = role.role_id and rm.menu_id = menu.menu_id);

