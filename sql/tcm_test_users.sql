-- TCM MVP test accounts (FOR TESTING ONLY; DO NOT USE IN PRODUCTION)
-- Default password is the same as the RuoYi demo admin (admin123). Change it in real deployments.
-- Run after tcm_mvp.sql on the same database (e.g. ry-vue).

-- Turn off captcha for easier API/automation login (set back to true in production)
UPDATE sys_config SET config_value = 'false' WHERE config_key = 'sys.account.captchaEnabled';

DELETE FROM sys_user_role WHERE user_id IN (200, 201);
DELETE FROM sys_user WHERE user_id IN (200, 201);

INSERT INTO sys_user (
  user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password,
  status, del_flag, login_ip, login_date, pwd_update_date, create_by, create_time, update_by, update_time, remark
) VALUES
(
  200, 103, 'tcm_enterprise', 'TCM Enterprise Test', '00', 'tcm_ent@test.local', '', '0', '',
  '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2',
  '0', '0', '', NULL, sysdate(), 'admin', sysdate(), '', NULL, 'TCM MVP test enterprise'
),
(
  201, 103, 'tcm_buyer', 'TCM Buyer Test', '00', 'tcm_buyer@test.local', '', '0', '',
  '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2',
  '0', '0', '', NULL, sysdate(), 'admin', sysdate(), '', NULL, 'TCM MVP test buyer'
);

INSERT INTO sys_user_role (user_id, role_id)
SELECT 200, r.role_id FROM sys_role r WHERE r.role_key = 'tcm_enterprise';

INSERT INTO sys_user_role (user_id, role_id)
SELECT 201, r.role_id FROM sys_role r WHERE r.role_key = 'tcm_buyer';
