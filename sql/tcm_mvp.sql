-- -----------------------------------------------------
-- TCM Traceability MVP init script (UTF-8 safe)
-- -----------------------------------------------------

-- 1) Tables
drop table if exists biz_tcm_base;
create table biz_tcm_base (
  base_id bigint(20) not null auto_increment comment 'Base ID',
  enterprise_user_id bigint(20) not null comment 'Enterprise user ID',
  base_name varchar(100) not null comment 'Base name',
  province varchar(50) default '' comment 'Province',
  city varchar(50) default '' comment 'City',
  district varchar(50) default '' comment 'District',
  detail_address varchar(255) default '' comment 'Detail address',
  area_size decimal(12,2) default 0 comment 'Area',
  soil_report_url varchar(255) default '' comment 'Soil report URL',
  water_report_url varchar(255) default '' comment 'Water report URL',
  status char(1) default '0' comment 'Status 0:normal 1:disabled',
  del_flag char(1) default '0' comment 'Delete flag 0:exists 2:deleted',
  create_by varchar(64) default '' comment 'Created by',
  create_time datetime comment 'Created time',
  update_by varchar(64) default '' comment 'Updated by',
  update_time datetime comment 'Updated time',
  remark varchar(500) default null comment 'Remark',
  primary key (base_id),
  key idx_base_enterprise_user_id (enterprise_user_id)
) engine=innodb comment='TCM base table';

drop table if exists biz_tcm_product;
create table biz_tcm_product (
  product_id bigint(20) not null auto_increment comment 'Product ID',
  enterprise_user_id bigint(20) not null comment 'Enterprise user ID',
  product_code varchar(64) not null comment 'Product code',
  product_name varchar(100) not null comment 'Product name',
  category_code varchar(64) default '' comment 'Category code',
  origin_place varchar(100) default '' comment 'Origin place',
  stock_quantity int(11) default 0 comment 'Stock quantity',
  status char(1) default '0' comment 'Status 0:normal 1:disabled',
  del_flag char(1) default '0' comment 'Delete flag 0:exists 2:deleted',
  create_by varchar(64) default '' comment 'Created by',
  create_time datetime comment 'Created time',
  update_by varchar(64) default '' comment 'Updated by',
  update_time datetime comment 'Updated time',
  remark varchar(500) default null comment 'Remark',
  primary key (product_id),
  unique key uk_tcm_product_code (product_code),
  key idx_product_enterprise_user_id (enterprise_user_id)
) engine=innodb comment='TCM product table';

drop table if exists biz_tcm_batch;
create table biz_tcm_batch (
  batch_id bigint(20) not null auto_increment comment 'Batch ID',
  enterprise_user_id bigint(20) not null comment 'Enterprise user ID',
  base_id bigint(20) not null comment 'Base ID',
  product_id bigint(20) not null comment 'Product ID',
  batch_no varchar(64) not null comment 'Batch number',
  sowing_date date comment 'Sowing date',
  harvest_date date comment 'Harvest date',
  process_stage varchar(50) default '' comment 'Current stage',
  publish_status char(1) default '0' comment '0:draft 1:published',
  status char(1) default '0' comment 'Status 0:normal 1:disabled',
  del_flag char(1) default '0' comment 'Delete flag 0:exists 2:deleted',
  create_by varchar(64) default '' comment 'Created by',
  create_time datetime comment 'Created time',
  update_by varchar(64) default '' comment 'Updated by',
  update_time datetime comment 'Updated time',
  remark varchar(500) default null comment 'Remark',
  primary key (batch_id),
  unique key uk_tcm_batch_no (batch_no),
  key idx_batch_enterprise_user_id (enterprise_user_id),
  key idx_batch_base_id (base_id),
  key idx_batch_product_id (product_id)
) engine=innodb comment='TCM batch table';

drop table if exists biz_tcm_process;
create table biz_tcm_process (
  process_id bigint(20) not null auto_increment comment 'Process ID',
  batch_id bigint(20) not null comment 'Batch ID',
  enterprise_user_id bigint(20) not null comment 'Enterprise user ID',
  process_type varchar(32) not null comment 'seed/plant/irrigate/fertilize/weed/harvest/process/package',
  process_content varchar(1000) default '' comment 'Process content',
  process_time datetime not null comment 'Process time',
  operator_name varchar(64) default '' comment 'Operator',
  del_flag char(1) default '0' comment 'Delete flag 0:exists 2:deleted',
  create_by varchar(64) default '' comment 'Created by',
  create_time datetime comment 'Created time',
  update_by varchar(64) default '' comment 'Updated by',
  update_time datetime comment 'Updated time',
  remark varchar(500) default null comment 'Remark',
  primary key (process_id),
  key idx_process_batch_id (batch_id),
  key idx_process_enterprise_user_id (enterprise_user_id)
) engine=innodb comment='TCM process table';

drop table if exists biz_tcm_trace_code;
create table biz_tcm_trace_code (
  trace_id bigint(20) not null auto_increment comment 'Trace ID',
  batch_id bigint(20) not null comment 'Batch ID',
  enterprise_user_id bigint(20) not null comment 'Enterprise user ID',
  trace_code varchar(64) not null comment 'Trace code UUID32',
  qr_code_url varchar(255) default '' comment 'QR URL',
  qr_code_base64 longtext comment 'QR image base64',
  status char(1) default '0' comment '0:active 1:inactive',
  del_flag char(1) default '0' comment 'Delete flag 0:exists 2:deleted',
  create_by varchar(64) default '' comment 'Created by',
  create_time datetime comment 'Created time',
  update_by varchar(64) default '' comment 'Updated by',
  update_time datetime comment 'Updated time',
  remark varchar(500) default null comment 'Remark',
  primary key (trace_id),
  unique key uk_tcm_trace_code (trace_code),
  unique key uk_tcm_trace_batch (batch_id),
  key idx_trace_enterprise_user_id (enterprise_user_id)
) engine=innodb comment='TCM trace code table';

drop table if exists biz_tcm_review;
create table biz_tcm_review (
  review_id bigint(20) not null auto_increment comment 'Review ID',
  batch_id bigint(20) not null comment 'Batch ID',
  trace_id bigint(20) default null comment 'Trace ID',
  buyer_user_id bigint(20) not null comment 'Buyer user ID',
  enterprise_user_id bigint(20) not null comment 'Enterprise user ID',
  product_score tinyint(4) not null comment 'Product score 1-5',
  enterprise_score tinyint(4) not null comment 'Enterprise score 1-5',
  review_content varchar(1000) default '' comment 'Review content',
  del_flag char(1) default '0' comment 'Delete flag 0:exists 2:deleted',
  create_by varchar(64) default '' comment 'Created by',
  create_time datetime comment 'Created time',
  update_by varchar(64) default '' comment 'Updated by',
  update_time datetime comment 'Updated time',
  remark varchar(500) default null comment 'Remark',
  primary key (review_id),
  key idx_review_batch_id (batch_id),
  key idx_review_buyer_user_id (buyer_user_id),
  key idx_review_enterprise_user_id (enterprise_user_id)
) engine=innodb comment='TCM review table';

-- 2) Roles
insert into sys_role(role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
select 'TCM Enterprise', 'tcm_enterprise', 11, '1', 1, 1, '0', '0', 'admin', sysdate(), 'TCM MVP role'
where not exists (select 1 from sys_role where role_key = 'tcm_enterprise');

insert into sys_role(role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
select 'TCM Buyer', 'tcm_buyer', 12, '1', 1, 1, '0', '0', 'admin', sysdate(), 'TCM MVP role'
where not exists (select 1 from sys_role where role_key = 'tcm_buyer');

-- 3) Menus
insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select 'TCM Traceability', 0, 6, 'tcm', null, '', '', 1, 0, 'M', '0', '0', '', 'guide', 'admin', sysdate(), 'TCM root menu'
where not exists (select 1 from sys_menu where path = 'tcm' and menu_type = 'M');

insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select 'Dashboard', m.menu_id, 0, 'dashboard', 'tcm/dashboard/index', '', '', 1, 0, 'C', '0', '0', 'biz:base:list', 'chart', 'admin', sysdate(), 'TCM enterprise dashboard'
from sys_menu m where m.path = 'tcm' and m.menu_type = 'M'
and not exists (select 1 from sys_menu c where c.path = 'dashboard' and c.component = 'tcm/dashboard/index');

insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select 'Base', m.menu_id, 1, 'base', 'tcm/base/index', '', '', 1, 0, 'C', '0', '0', 'biz:base:list', 'tree', 'admin', sysdate(), 'Base menu'
from sys_menu m where m.path = 'tcm' and m.menu_type = 'M'
and not exists (select 1 from sys_menu c where c.path = 'base' and c.component = 'tcm/base/index');

insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select 'Product', m.menu_id, 2, 'product', 'tcm/product/index', '', '', 1, 0, 'C', '0', '0', 'biz:product:list', 'example', 'admin', sysdate(), 'Product menu'
from sys_menu m where m.path = 'tcm' and m.menu_type = 'M'
and not exists (select 1 from sys_menu c where c.path = 'product' and c.component = 'tcm/product/index');

insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select 'Batch', m.menu_id, 3, 'batch', 'tcm/batch/index', '', '', 1, 0, 'C', '0', '0', 'biz:batch:list', 'date', 'admin', sysdate(), 'Batch menu'
from sys_menu m where m.path = 'tcm' and m.menu_type = 'M'
and not exists (select 1 from sys_menu c where c.path = 'batch' and c.component = 'tcm/batch/index');

insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select 'Process', m.menu_id, 4, 'process', 'tcm/process/index', '', '', 1, 0, 'C', '0', '0', 'biz:process:list', 'form', 'admin', sysdate(), 'Process menu'
from sys_menu m where m.path = 'tcm' and m.menu_type = 'M'
and not exists (select 1 from sys_menu c where c.path = 'process' and c.component = 'tcm/process/index');

insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select 'Trace Query', m.menu_id, 5, 'trace', 'tcm/trace/index', '', '', 1, 0, 'C', '0', '0', 'biz:trace:list', 'search', 'admin', sysdate(), 'Trace query menu'
from sys_menu m where m.path = 'tcm' and m.menu_type = 'M'
and not exists (select 1 from sys_menu c where c.path = 'trace' and c.component = 'tcm/trace/index');

insert into sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
select 'Review', m.menu_id, 6, 'review', 'tcm/review/index', '', '', 1, 0, 'C', '0', '0', 'biz:review:list', 'message', 'admin', sysdate(), 'Review menu'
from sys_menu m where m.path = 'tcm' and m.menu_type = 'M'
and not exists (select 1 from sys_menu c where c.path = 'review' and c.component = 'tcm/review/index');

-- 4) Buttons
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Base Query',b.menu_id,1,'#','','','',1,0,'F','0','0','biz:base:query','#','admin',sysdate(),''
from sys_menu b where b.path='base' and b.component='tcm/base/index' and not exists (select 1 from sys_menu f where f.perms='biz:base:query');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Base Add',b.menu_id,2,'#','','','',1,0,'F','0','0','biz:base:add','#','admin',sysdate(),''
from sys_menu b where b.path='base' and b.component='tcm/base/index' and not exists (select 1 from sys_menu f where f.perms='biz:base:add');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Base Edit',b.menu_id,3,'#','','','',1,0,'F','0','0','biz:base:edit','#','admin',sysdate(),''
from sys_menu b where b.path='base' and b.component='tcm/base/index' and not exists (select 1 from sys_menu f where f.perms='biz:base:edit');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Base Remove',b.menu_id,4,'#','','','',1,0,'F','0','0','biz:base:remove','#','admin',sysdate(),''
from sys_menu b where b.path='base' and b.component='tcm/base/index' and not exists (select 1 from sys_menu f where f.perms='biz:base:remove');

insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Product Query',b.menu_id,1,'#','','','',1,0,'F','0','0','biz:product:query','#','admin',sysdate(),''
from sys_menu b where b.path='product' and b.component='tcm/product/index' and not exists (select 1 from sys_menu f where f.perms='biz:product:query');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Product Add',b.menu_id,2,'#','','','',1,0,'F','0','0','biz:product:add','#','admin',sysdate(),''
from sys_menu b where b.path='product' and b.component='tcm/product/index' and not exists (select 1 from sys_menu f where f.perms='biz:product:add');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Product Edit',b.menu_id,3,'#','','','',1,0,'F','0','0','biz:product:edit','#','admin',sysdate(),''
from sys_menu b where b.path='product' and b.component='tcm/product/index' and not exists (select 1 from sys_menu f where f.perms='biz:product:edit');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Product Remove',b.menu_id,4,'#','','','',1,0,'F','0','0','biz:product:remove','#','admin',sysdate(),''
from sys_menu b where b.path='product' and b.component='tcm/product/index' and not exists (select 1 from sys_menu f where f.perms='biz:product:remove');

insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Batch Query',b.menu_id,1,'#','','','',1,0,'F','0','0','biz:batch:query','#','admin',sysdate(),''
from sys_menu b where b.path='batch' and b.component='tcm/batch/index' and not exists (select 1 from sys_menu f where f.perms='biz:batch:query');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Batch Add',b.menu_id,2,'#','','','',1,0,'F','0','0','biz:batch:add','#','admin',sysdate(),''
from sys_menu b where b.path='batch' and b.component='tcm/batch/index' and not exists (select 1 from sys_menu f where f.perms='biz:batch:add');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Batch Edit',b.menu_id,3,'#','','','',1,0,'F','0','0','biz:batch:edit','#','admin',sysdate(),''
from sys_menu b where b.path='batch' and b.component='tcm/batch/index' and not exists (select 1 from sys_menu f where f.perms='biz:batch:edit');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Batch Remove',b.menu_id,4,'#','','','',1,0,'F','0','0','biz:batch:remove','#','admin',sysdate(),''
from sys_menu b where b.path='batch' and b.component='tcm/batch/index' and not exists (select 1 from sys_menu f where f.perms='biz:batch:remove');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Batch Publish',b.menu_id,5,'#','','','',1,0,'F','0','0','biz:batch:publish','#','admin',sysdate(),''
from sys_menu b where b.path='batch' and b.component='tcm/batch/index' and not exists (select 1 from sys_menu f where f.perms='biz:batch:publish');

insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Process List',b.menu_id,1,'#','','','',1,0,'F','0','0','biz:process:list','#','admin',sysdate(),''
from sys_menu b where b.path='process' and b.component='tcm/process/index' and not exists (select 1 from sys_menu f where f.perms='biz:process:list');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Process Add',b.menu_id,2,'#','','','',1,0,'F','0','0','biz:process:add','#','admin',sysdate(),''
from sys_menu b where b.path='process' and b.component='tcm/process/index' and not exists (select 1 from sys_menu f where f.perms='biz:process:add');

insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Trace List',b.menu_id,1,'#','','','',1,0,'F','0','0','biz:trace:list','#','admin',sysdate(),''
from sys_menu b where b.path='trace' and b.component='tcm/trace/index' and not exists (select 1 from sys_menu f where f.perms='biz:trace:list');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Trace Query',b.menu_id,2,'#','','','',1,0,'F','0','0','biz:trace:query','#','admin',sysdate(),''
from sys_menu b where b.path='trace' and b.component='tcm/trace/index' and not exists (select 1 from sys_menu f where f.perms='biz:trace:query');

insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Review List',b.menu_id,1,'#','','','',1,0,'F','0','0','biz:review:list','#','admin',sysdate(),''
from sys_menu b where b.path='review' and b.component='tcm/review/index' and not exists (select 1 from sys_menu f where f.perms='biz:review:list');
insert into sys_menu(menu_name,parent_id,order_num,path,component,query,route_name,is_frame,is_cache,menu_type,visible,status,perms,icon,create_by,create_time,remark)
select 'Review Add',b.menu_id,2,'#','','','',1,0,'F','0','0','biz:review:add','#','admin',sysdate(),''
from sys_menu b where b.path='review' and b.component='tcm/review/index' and not exists (select 1 from sys_menu f where f.perms='biz:review:add');

-- 5) Role-menu binding
insert into sys_role_menu(role_id, menu_id)
select role.role_id, menu.menu_id
from sys_role role
join sys_menu menu on menu.path in ('tcm', 'dashboard', 'base', 'product', 'batch', 'process', 'trace')
where role.role_key = 'tcm_enterprise'
  and not exists (select 1 from sys_role_menu rm where rm.role_id = role.role_id and rm.menu_id = menu.menu_id);

insert into sys_role_menu(role_id, menu_id)
select role.role_id, menu.menu_id
from sys_role role
join sys_menu menu on menu.path in ('tcm', 'trace', 'review')
where role.role_key = 'tcm_buyer'
  and not exists (select 1 from sys_role_menu rm where rm.role_id = role.role_id and rm.menu_id = menu.menu_id);

insert into sys_role_menu(role_id, menu_id)
select role.role_id, menu.menu_id
from sys_role role
join sys_menu menu on menu.perms like 'biz:%'
where role.role_key = 'tcm_enterprise'
  and menu.perms not like 'biz:review:%'
  and not exists (select 1 from sys_role_menu rm where rm.role_id = role.role_id and rm.menu_id = menu.menu_id);

insert into sys_role_menu(role_id, menu_id)
select role.role_id, menu.menu_id
from sys_role role
join sys_menu menu on menu.perms in ('biz:trace:list','biz:trace:query','biz:review:list','biz:review:add')
where role.role_key = 'tcm_buyer'
  and not exists (select 1 from sys_role_menu rm where rm.role_id = role.role_id and rm.menu_id = menu.menu_id);
