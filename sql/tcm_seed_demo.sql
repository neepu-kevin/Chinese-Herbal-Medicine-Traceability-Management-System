-- Demo data for TCM Traceability MVP
-- UTF-8. Run after `tcm_mvp.sql` and (optionally) `tcm_test_users.sql`.
-- Target DB: your RuoYi database (e.g. ry-vue).

-- Resolve demo users (created by tcm_test_users.sql)
set @ent_uid := (select user_id from sys_user where user_name = 'tcm_enterprise' limit 1);
set @buyer_uid := (select user_id from sys_user where user_name = 'tcm_buyer' limit 1);

-- ----------------------------
-- 1) Base
-- ----------------------------
insert into biz_tcm_base(enterprise_user_id, base_name, province, city, district, detail_address, area_size, soil_report_url, water_report_url, status, del_flag, create_by, create_time, update_by, update_time, remark)
select @ent_uid, '���ϵ���ҩ��ʾ������', '����ʡ', '������', '�ʹ���', '���Ͻֵ�ʾ���ؿ�A��', 120.50,
       'https://example.local/soil-report.pdf', 'https://example.local/water-report.pdf',
       '0', '0', 'tcm_enterprise', sysdate(), '', null, '������ʾ�����ؽ��������ΰ�'
where @ent_uid is not null
  and not exists (select 1 from biz_tcm_base where base_name = '���ϵ���ҩ��ʾ������');

set @base_id := (select base_id from biz_tcm_base where base_name = '���ϵ���ҩ��ʾ������' order by base_id desc limit 1);

-- ----------------------------
-- 2) Product
-- ----------------------------
insert into biz_tcm_product(enterprise_user_id, product_code, product_name, category_code, origin_place, stock_quantity, status, del_flag, create_by, create_time, update_by, update_time, remark)
select @ent_uid, 'P-DEMO-ANGELICA', '���飨��Ƭ��', 'TCM', '����', 800, '0', '0', 'tcm_enterprise', sysdate(), '', null, '������ʾ����Ʒ����������Ƽ�'
where @ent_uid is not null
  and not exists (select 1 from biz_tcm_product where product_code = 'P-DEMO-ANGELICA');

set @product_id := (select product_id from biz_tcm_product where product_code = 'P-DEMO-ANGELICA' limit 1);

-- ----------------------------
-- 3) Batch
-- ----------------------------
insert into biz_tcm_batch(enterprise_user_id, base_id, product_id, batch_no, sowing_date, harvest_date, process_stage, publish_status, status, del_flag, create_by, create_time, update_by, update_time, remark)
select @ent_uid, @base_id, @product_id, 'B-DEMO-20260421-001', '2026-03-01', '2026-04-10', 'package',
       '1', '0', '0', 'tcm_enterprise', sysdate(), '', null, '������ʾ��������Դ��·���ѷ�����'
where @ent_uid is not null and @base_id is not null and @product_id is not null
  and not exists (select 1 from biz_tcm_batch where batch_no = 'B-DEMO-20260421-001');

set @batch_id := (select batch_id from biz_tcm_batch where batch_no = 'B-DEMO-20260421-001' limit 1);

-- ----------------------------
-- 4) Process records (timeline)
-- ----------------------------
insert into biz_tcm_process(batch_id, enterprise_user_id, process_type, process_content, process_time, operator_name, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @ent_uid, 'seed', 'ѡ�õ��ز�������/���磬��������������Ǽǡ�', '2026-03-01 09:00:00', 'tcm_enterprise', '0', 'tcm_enterprise', sysdate(), '', null, null
where @batch_id is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_process where batch_id = @batch_id and process_type = 'seed');

insert into biz_tcm_process(batch_id, enterprise_user_id, process_type, process_content, process_time, operator_name, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @ent_uid, 'plant', '������¢�����淶���֣���¼�����ܶ���ؿ���Ϣ��', '2026-03-05 10:30:00', 'tcm_enterprise', '0', 'tcm_enterprise', sysdate(), '', null, null
where @batch_id is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_process where batch_id = @batch_id and process_type = 'plant');

insert into biz_tcm_process(batch_id, enterprise_user_id, process_type, process_content, process_time, operator_name, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @ent_uid, 'irrigate', '�ι��ȣ���¼ˮԴ����ʱ����', '2026-03-12 16:00:00', 'tcm_enterprise', '0', 'tcm_enterprise', sysdate(), '', null, null
where @batch_id is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_process where batch_id = @batch_id and process_type = 'irrigate');

insert into biz_tcm_process(batch_id, enterprise_user_id, process_type, process_content, process_time, operator_name, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @ent_uid, 'fertilize', '�л���ʩ�ã���¼������ʩ�÷�ʽ��', '2026-03-18 09:40:00', 'tcm_enterprise', '0', 'tcm_enterprise', sysdate(), '', null, null
where @batch_id is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_process where batch_id = @batch_id and process_type = 'fertilize');

insert into biz_tcm_process(batch_id, enterprise_user_id, process_type, process_content, process_time, operator_name, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @ent_uid, 'weed', '�˹����ݣ���¼��ҵ��Χ��ʱ�䡣', '2026-03-26 15:20:00', 'tcm_enterprise', '0', 'tcm_enterprise', sysdate(), '', null, null
where @batch_id is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_process where batch_id = @batch_id and process_type = 'weed');

insert into biz_tcm_process(batch_id, enterprise_user_id, process_type, process_content, process_time, operator_name, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @ent_uid, 'harvest', '����׼���գ���¼�����������������', '2026-04-10 08:10:00', 'tcm_enterprise', '0', 'tcm_enterprise', sysdate(), '', null, null
where @batch_id is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_process where batch_id = @batch_id and process_type = 'harvest');

insert into biz_tcm_process(batch_id, enterprise_user_id, process_type, process_content, process_time, operator_name, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @ent_uid, 'process', '��ϴ�����ơ�����ȳ��ӹ����ڼ�¼��', '2026-04-12 13:30:00', 'tcm_enterprise', '0', 'tcm_enterprise', sysdate(), '', null, null
where @batch_id is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_process where batch_id = @batch_id and process_type = 'process');

insert into biz_tcm_process(batch_id, enterprise_user_id, process_type, process_content, process_time, operator_name, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @ent_uid, 'package', '��Ʒ��װ����¼��װ��������������Ϣ��', '2026-04-15 11:00:00', 'tcm_enterprise', '0', 'tcm_enterprise', sysdate(), '', null, null
where @batch_id is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_process where batch_id = @batch_id and process_type = 'package');

-- ----------------------------
-- 5) Trace code (published)
-- ----------------------------
insert into biz_tcm_trace_code(batch_id, enterprise_user_id, trace_code, qr_code_url, qr_code_base64, status, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @ent_uid, 'DEMO20260421TRACE000000000000000', '', '', '0', '0', 'tcm_enterprise', sysdate(), '', null, '������ʾ����Դ���ѯ'
where @batch_id is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_trace_code where trace_code = 'DEMO20260421TRACE000000000000000');

set @trace_id := (select trace_id from biz_tcm_trace_code where trace_code = 'DEMO20260421TRACE000000000000000' limit 1);

-- ----------------------------
-- 6) Review (buyer)
-- ----------------------------
insert into biz_tcm_review(batch_id, trace_id, buyer_user_id, enterprise_user_id, product_score, enterprise_score, review_content, del_flag, create_by, create_time, update_by, update_time, remark)
select @batch_id, @trace_id, @buyer_uid, @ent_uid, 5, 5, 'Ʒ���ȶ�����Դ��Ϣ������ֵ���Ƽ���', '0', 'tcm_buyer', sysdate(), '', null, '��ʾ�����ۻ���չʾ'
where @batch_id is not null and @trace_id is not null and @buyer_uid is not null and @ent_uid is not null
  and not exists (select 1 from biz_tcm_review where batch_id = @batch_id and buyer_user_id = @buyer_uid);

-- Tips:
-- 1) After importing, you can query trace code:
--    DEMO20260421TRACE000000000000000
-- 2) If you want a real QR image, publish the batch from UI to generate it.

