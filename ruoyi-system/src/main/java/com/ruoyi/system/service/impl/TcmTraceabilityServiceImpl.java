package com.ruoyi.system.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.service.ITcmTraceabilityService;
import javax.imageio.ImageIO;

@Service
public class TcmTraceabilityServiceImpl implements ITcmTraceabilityService
{
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> listBase(String userName)
    {
        return jdbcTemplate.queryForList(
                "select * from biz_tcm_base where del_flag = '0' and create_by = ? order by base_id desc", userName);
    }

    @Override
    public Map<String, Object> getBase(Long id, String userName)
    {
        return queryOne("select * from biz_tcm_base where base_id = ? and del_flag = '0' and create_by = ?", id, userName);
    }

    @Override
    public int addBase(Map<String, Object> data, String userName, Long userId)
    {
        return jdbcTemplate.update(
                "insert into biz_tcm_base(enterprise_user_id,base_name,province,city,district,detail_address,area_size,soil_report_url,water_report_url,status,create_by,create_time,remark) values(?,?,?,?,?,?,?,?,?,?,?,now(),?)",
                userId, str(data, "baseName"), str(data, "province"), str(data, "city"), str(data, "district"),
                str(data, "detailAddress"), decimal(data, "areaSize"), str(data, "soilReportUrl"), str(data, "waterReportUrl"), "0",
                userName, str(data, "remark"));
    }

    @Override
    public int editBase(Map<String, Object> data, String userName, Long userId)
    {
        return jdbcTemplate.update(
                "update biz_tcm_base set base_name=?,province=?,city=?,district=?,detail_address=?,area_size=?,soil_report_url=?,water_report_url=?,update_by=?,update_time=now(),remark=? where base_id=? and create_by=? and enterprise_user_id=? and del_flag='0'",
                str(data, "baseName"), str(data, "province"), str(data, "city"), str(data, "district"), str(data, "detailAddress"),
                decimal(data, "areaSize"), str(data, "soilReportUrl"), str(data, "waterReportUrl"), userName, str(data, "remark"),
                longVal(data, "baseId"), userName, userId);
    }

    @Override
    public int removeBase(Long id, String userName)
    {
        return jdbcTemplate.update("update biz_tcm_base set del_flag='2' where base_id=? and create_by=? and del_flag='0'", id, userName);
    }

    @Override
    public List<Map<String, Object>> listProduct(String userName)
    {
        return jdbcTemplate.queryForList(
                "select * from biz_tcm_product where del_flag='0' and create_by=? order by product_id desc", userName);
    }

    @Override
    public Map<String, Object> getProduct(Long id, String userName)
    {
        return queryOne("select * from biz_tcm_product where product_id=? and create_by=? and del_flag='0'", id, userName);
    }

    @Override
    public int addProduct(Map<String, Object> data, String userName, Long userId)
    {
        return jdbcTemplate.update(
                "insert into biz_tcm_product(enterprise_user_id,product_code,product_name,category_code,origin_place,stock_quantity,status,create_by,create_time,remark) values(?,?,?,?,?,?,?,?,now(),?)",
                userId, str(data, "productCode"), str(data, "productName"), str(data, "categoryCode"), str(data, "originPlace"),
                intVal(data, "stockQuantity"), "0", userName, str(data, "remark"));
    }

    @Override
    public int editProduct(Map<String, Object> data, String userName, Long userId)
    {
        return jdbcTemplate.update(
                "update biz_tcm_product set product_code=?,product_name=?,category_code=?,origin_place=?,stock_quantity=?,update_by=?,update_time=now(),remark=? where product_id=? and create_by=? and enterprise_user_id=? and del_flag='0'",
                str(data, "productCode"), str(data, "productName"), str(data, "categoryCode"), str(data, "originPlace"),
                intVal(data, "stockQuantity"), userName, str(data, "remark"), longVal(data, "productId"), userName, userId);
    }

    @Override
    public int removeProduct(Long id, String userName)
    {
        return jdbcTemplate.update("update biz_tcm_product set del_flag='2' where product_id=? and create_by=? and del_flag='0'", id, userName);
    }

    @Override
    public List<Map<String, Object>> listBatch(String userName)
    {
        String sql = "select b.*,p.product_name,base.base_name,t.trace_code from biz_tcm_batch b "
                + "left join biz_tcm_product p on b.product_id=p.product_id "
                + "left join biz_tcm_base base on b.base_id=base.base_id "
                + "left join biz_tcm_trace_code t on b.batch_id=t.batch_id and t.del_flag='0' "
                + "where b.del_flag='0' and b.create_by=? order by b.batch_id desc";
        return jdbcTemplate.queryForList(sql, userName);
    }

    @Override
    public Map<String, Object> getBatch(Long id, String userName)
    {
        return queryOne("select * from biz_tcm_batch where batch_id=? and create_by=? and del_flag='0'", id, userName);
    }

    @Override
    public int addBatch(Map<String, Object> data, String userName, Long userId)
    {
        return jdbcTemplate.update(
                "insert into biz_tcm_batch(enterprise_user_id,base_id,product_id,batch_no,sowing_date,harvest_date,process_stage,publish_status,status,create_by,create_time,remark) values(?,?,?,?,?,?,?,?,?,?,now(),?)",
                userId, longVal(data, "baseId"), longVal(data, "productId"), str(data, "batchNo"), sqlDateOrNull(data, "sowingDate"),
                sqlDateOrNull(data, "harvestDate"), str(data, "processStage"), "0", "0", userName, str(data, "remark"));
    }

    @Override
    public int editBatch(Map<String, Object> data, String userName, Long userId)
    {
        return jdbcTemplate.update(
                "update biz_tcm_batch set base_id=?,product_id=?,batch_no=?,sowing_date=?,harvest_date=?,process_stage=?,update_by=?,update_time=now(),remark=? where batch_id=? and create_by=? and enterprise_user_id=? and del_flag='0' and publish_status='0'",
                longVal(data, "baseId"), longVal(data, "productId"), str(data, "batchNo"), sqlDateOrNull(data, "sowingDate"),
                sqlDateOrNull(data, "harvestDate"), str(data, "processStage"), userName, str(data, "remark"), longVal(data, "batchId"), userName, userId);
    }

    @Override
    public int removeBatch(Long id, String userName)
    {
        return jdbcTemplate.update(
                "update biz_tcm_batch set del_flag='2' where batch_id=? and create_by=? and del_flag='0' and publish_status='0'", id,
                userName);
    }

    @Override
    public Map<String, Object> publishBatch(Long batchId, String userName, Long userId)
    {
        Map<String, Object> batch = queryOne(
                "select * from biz_tcm_batch where batch_id=? and create_by=? and enterprise_user_id=? and del_flag='0'",
                batchId, userName, userId);
        if (batch == null)
        {
            throw new ServiceException("Batch not found or no permission");
        }
        if ("1".equals(String.valueOf(batch.get("publish_status"))))
        {
            Map<String, Object> exist = queryOne("select trace_code,qr_code_base64 from biz_tcm_trace_code where batch_id=? and del_flag='0'", batchId);
            if (exist != null)
            {
                return exist;
            }
        }
        String traceCode = UUID.randomUUID().toString().replace("-", "");
        String qrBase64 = buildPseudoQrBase64(traceCode);
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement(
                    "insert into biz_tcm_trace_code(batch_id,enterprise_user_id,trace_code,qr_code_url,qr_code_base64,status,create_by,create_time) values(?,?,?,?,?,'0',?,now())",
                    new String[] {"trace_id"});
            ps.setLong(1, batchId);
            ps.setLong(2, userId);
            ps.setString(3, traceCode);
            ps.setString(4, "/tcm/public/trace/" + traceCode);
            ps.setString(5, qrBase64);
            ps.setString(6, userName);
            return ps;
        }, holder);
        jdbcTemplate.update("update biz_tcm_batch set publish_status='1',update_by=?,update_time=now() where batch_id=?", userName, batchId);
        Map<String, Object> result = new HashMap<>();
        result.put("traceCode", traceCode);
        result.put("qrCodeBase64", qrBase64);
        return result;
    }

    @Override
    public List<Map<String, Object>> listProcess(String userName, Long batchId)
    {
        String sql = "select * from biz_tcm_process where del_flag='0' and create_by=?";
        if (batchId != null)
        {
            sql += " and batch_id=" + batchId;
        }
        sql += " order by process_time desc, process_id desc";
        return jdbcTemplate.queryForList(sql, userName);
    }

    @Override
    public int addProcess(Map<String, Object> data, String userName, Long userId)
    {
        return jdbcTemplate.update(
                "insert into biz_tcm_process(batch_id,enterprise_user_id,process_type,process_content,process_time,operator_name,create_by,create_time,remark) values(?,?,?,?,?,?,?,now(),?)",
                longVal(data, "batchId"), userId, str(data, "processType"), str(data, "processContent"),
                str(data, "processTime"), str(data, "operatorName"), userName, str(data, "remark"));
    }

    @Override
    public List<Map<String, Object>> listReview(String userName, Long batchId)
    {
        String sql = "select * from biz_tcm_review where del_flag='0'";
        if (batchId != null)
        {
            sql += " and batch_id=" + batchId;
        }
        return jdbcTemplate.queryForList(sql + " order by review_id desc");
    }

    @Override
    public int addReview(Map<String, Object> data, String userName, Long userId)
    {
        Long batchId = longVal(data, "batchId");
        Map<String, Object> batch = queryOne("select enterprise_user_id from biz_tcm_batch where batch_id=? and del_flag='0' and publish_status='1'", batchId);
        if (batch == null)
        {
            throw new ServiceException("Only published batches can be reviewed");
        }
        Long enterpriseUserId = ((Number) batch.get("enterprise_user_id")).longValue();
        Map<String, Object> trace = queryOne("select trace_id from biz_tcm_trace_code where batch_id=? and del_flag='0'", batchId);
        return jdbcTemplate.update(
                "insert into biz_tcm_review(batch_id,trace_id,buyer_user_id,enterprise_user_id,product_score,enterprise_score,review_content,create_by,create_time,remark) values(?,?,?,?,?,?,?,?,now(),?)",
                batchId, trace == null ? null : ((Number) trace.get("trace_id")).longValue(), userId, enterpriseUserId,
                intVal(data, "productScore"), intVal(data, "enterpriseScore"), str(data, "reviewContent"), userName, str(data, "remark"));
    }

    @Override
    public Map<String, Object> queryTrace(String traceCode)
    {
        String detailSql = "select t.trace_code,t.qr_code_base64,b.batch_id,b.batch_no,b.sowing_date,b.harvest_date,b.process_stage,"
                + "p.product_id,p.product_name,p.product_code,p.origin_place,"
                + "base.base_id,base.base_name,base.province,base.city,base.district,base.detail_address "
                + "from biz_tcm_trace_code t "
                + "join biz_tcm_batch b on t.batch_id=b.batch_id and b.del_flag='0' "
                + "join biz_tcm_product p on b.product_id=p.product_id and p.del_flag='0' "
                + "join biz_tcm_base base on b.base_id=base.base_id and base.del_flag='0' "
                + "where t.trace_code=? and t.del_flag='0' and t.status='0'";
        Map<String, Object> detail = queryOne(detailSql, traceCode);
        if (detail == null)
        {
            throw new ServiceException("Trace code not found or inactive");
        }
        Long batchId = ((Number) detail.get("batch_id")).longValue();
        List<Map<String, Object>> processList = jdbcTemplate.queryForList(
                "select process_type,process_content,process_time,operator_name from biz_tcm_process where batch_id=? and del_flag='0' order by process_time asc,process_id asc",
                batchId);
        Map<String, Object> score = jdbcTemplate.queryForObject(
                "select ifnull(avg(product_score),0) as product_score_avg, ifnull(avg(enterprise_score),0) as enterprise_score_avg, count(1) as review_count from biz_tcm_review where batch_id=? and del_flag='0'",
                mapRowMapper(), batchId);
        detail.put("processList", processList);
        detail.put("score", score);
        return detail;
    }

    private String str(Map<String, Object> data, String key)
    {
        Object value = data == null ? null : data.get(key);
        return value == null ? "" : String.valueOf(value);
    }

    private Date sqlDateOrNull(Map<String, Object> data, String key)
    {
        String value = str(data, key);
        if (value == null || value.isEmpty())
        {
            return null;
        }
        return Date.valueOf(value);
    }

    private Long longVal(Map<String, Object> data, String key)
    {
        Object value = data == null ? null : data.get(key);
        if (value == null || "".equals(value))
        {
            return null;
        }
        if (value instanceof Number number)
        {
            return number.longValue();
        }
        return Long.parseLong(String.valueOf(value));
    }

    private Integer intVal(Map<String, Object> data, String key)
    {
        Object value = data == null ? null : data.get(key);
        if (value == null || "".equals(value))
        {
            return 0;
        }
        if (value instanceof Number number)
        {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }

    private BigDecimal decimal(Map<String, Object> data, String key)
    {
        Object value = data == null ? null : data.get(key);
        if (value == null || "".equals(value))
        {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal decimal)
        {
            return decimal;
        }
        return new BigDecimal(String.valueOf(value));
    }

    private Map<String, Object> queryOne(String sql, Object... args)
    {
        try
        {
            return jdbcTemplate.queryForObject(sql, mapRowMapper(), args);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    private RowMapper<Map<String, Object>> mapRowMapper()
    {
        return (rs, rowNum) -> toMap(rs);
    }

    private Map<String, Object> toMap(ResultSet rs) throws SQLException
    {
        Map<String, Object> row = new HashMap<>();
        int count = rs.getMetaData().getColumnCount();
        for (int i = 1; i <= count; i++)
        {
            row.put(rs.getMetaData().getColumnLabel(i), rs.getObject(i));
        }
        return row;
    }

    private String buildPseudoQrBase64(String traceCode)
    {
        try
        {
            BufferedImage image = new BufferedImage(320, 320, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, 320, 320);
            g2.setColor(Color.BLACK);
            g2.drawRect(10, 10, 300, 300);
            g2.drawString("TCM TRACE", 120, 40);
            g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
            g2.drawString(traceCode.substring(0, 16), 40, 160);
            g2.drawString(traceCode.substring(16), 40, 180);
            g2.drawString(LocalDateTime.now().format(DATETIME_FORMATTER), 80, 300);
            g2.dispose();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(image, "png", output);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(output.toByteArray());
        }
        catch (Exception ex)
        {
            throw new ServiceException("QR image generation failed");
        }
    }
}
