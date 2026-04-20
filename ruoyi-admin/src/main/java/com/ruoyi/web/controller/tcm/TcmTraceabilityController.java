package com.ruoyi.web.controller.tcm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ITcmTraceabilityService;

@RestController
@RequestMapping("/tcm")
public class TcmTraceabilityController extends BaseController
{
    @Autowired
    private ITcmTraceabilityService tcmTraceabilityService;

    @PreAuthorize("@ss.hasPermi('biz:base:list')")
    @GetMapping("/base/list")
    public TableDataInfo baseList()
    {
        List<Map<String, Object>> list = tcmTraceabilityService.listBase(getUsername());
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('biz:base:query')")
    @GetMapping("/base/{id}")
    public AjaxResult baseInfo(@PathVariable Long id)
    {
        return success(tcmTraceabilityService.getBase(id, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('biz:base:add')")
    @Log(title = "TCM base", businessType = BusinessType.INSERT)
    @PostMapping("/base")
    public AjaxResult baseAdd(@RequestBody Map<String, Object> data)
    {
        return toAjax(tcmTraceabilityService.addBase(data, getUsername(), getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('biz:base:edit')")
    @Log(title = "TCM base", businessType = BusinessType.UPDATE)
    @PutMapping("/base")
    public AjaxResult baseEdit(@RequestBody Map<String, Object> data)
    {
        return toAjax(tcmTraceabilityService.editBase(data, getUsername(), getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('biz:base:remove')")
    @Log(title = "TCM base", businessType = BusinessType.DELETE)
    @DeleteMapping("/base/{id}")
    public AjaxResult baseRemove(@PathVariable Long id)
    {
        return toAjax(tcmTraceabilityService.removeBase(id, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('biz:product:list')")
    @GetMapping("/product/list")
    public TableDataInfo productList()
    {
        List<Map<String, Object>> list = tcmTraceabilityService.listProduct(getUsername());
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('biz:product:query')")
    @GetMapping("/product/{id}")
    public AjaxResult productInfo(@PathVariable Long id)
    {
        return success(tcmTraceabilityService.getProduct(id, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('biz:product:add')")
    @PostMapping("/product")
    public AjaxResult productAdd(@RequestBody Map<String, Object> data)
    {
        return toAjax(tcmTraceabilityService.addProduct(data, getUsername(), getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('biz:product:edit')")
    @PutMapping("/product")
    public AjaxResult productEdit(@RequestBody Map<String, Object> data)
    {
        return toAjax(tcmTraceabilityService.editProduct(data, getUsername(), getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('biz:product:remove')")
    @DeleteMapping("/product/{id}")
    public AjaxResult productRemove(@PathVariable Long id)
    {
        return toAjax(tcmTraceabilityService.removeProduct(id, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('biz:batch:list')")
    @GetMapping("/batch/list")
    public TableDataInfo batchList()
    {
        List<Map<String, Object>> list = tcmTraceabilityService.listBatch(getUsername());
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('biz:batch:query')")
    @GetMapping("/batch/{id}")
    public AjaxResult batchInfo(@PathVariable Long id)
    {
        return success(tcmTraceabilityService.getBatch(id, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('biz:batch:add')")
    @PostMapping("/batch")
    public AjaxResult batchAdd(@RequestBody Map<String, Object> data)
    {
        return toAjax(tcmTraceabilityService.addBatch(data, getUsername(), getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('biz:batch:edit')")
    @PutMapping("/batch")
    public AjaxResult batchEdit(@RequestBody Map<String, Object> data)
    {
        return toAjax(tcmTraceabilityService.editBatch(data, getUsername(), getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('biz:batch:remove')")
    @DeleteMapping("/batch/{id}")
    public AjaxResult batchRemove(@PathVariable Long id)
    {
        return toAjax(tcmTraceabilityService.removeBatch(id, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('biz:batch:publish')")
    @PostMapping("/batch/{id}/publish")
    public AjaxResult batchPublish(@PathVariable Long id)
    {
        return success(tcmTraceabilityService.publishBatch(id, getUsername(), getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('biz:process:list')")
    @GetMapping("/process/list")
    public TableDataInfo processList(@RequestParam(required = false) Long batchId)
    {
        List<Map<String, Object>> list = tcmTraceabilityService.listProcess(getUsername(), batchId);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('biz:process:add')")
    @PostMapping("/process")
    public AjaxResult processAdd(@RequestBody Map<String, Object> data)
    {
        return toAjax(tcmTraceabilityService.addProcess(data, getUsername(), getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('biz:review:list')")
    @GetMapping("/review/list")
    public TableDataInfo reviewList(@RequestParam(required = false) Long batchId)
    {
        List<Map<String, Object>> list = tcmTraceabilityService.listReview(getUsername(), batchId);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('biz:review:add')")
    @PostMapping("/review")
    public AjaxResult reviewAdd(@RequestBody Map<String, Object> data)
    {
        return toAjax(tcmTraceabilityService.addReview(data, getUsername(), getUserId()));
    }

    @PreAuthorize("@ss.hasPermi('biz:trace:query')")
    @GetMapping("/trace/query")
    public AjaxResult traceQuery(@RequestParam String traceCode)
    {
        return success(tcmTraceabilityService.queryTrace(traceCode));
    }

    @Anonymous
    @GetMapping("/public/trace/{traceCode}")
    public AjaxResult publicTraceQuery(@PathVariable String traceCode)
    {
        return success(tcmTraceabilityService.queryTrace(traceCode));
    }

    @PreAuthorize("@ss.hasPermi('biz:trace:list')")
    @GetMapping("/trace/list")
    public TableDataInfo traceList()
    {
        List<Map<String, Object>> list = tcmTraceabilityService.listBatch(getUsername());
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('biz:base:list')")
    @GetMapping("/dashboard/summary")
    public AjaxResult dashboardSummary()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("baseCount", tcmTraceabilityService.listBase(getUsername()).size());
        data.put("productCount", tcmTraceabilityService.listProduct(getUsername()).size());
        data.put("batchCount", tcmTraceabilityService.listBatch(getUsername()).size());
        return success(data);
    }
}
