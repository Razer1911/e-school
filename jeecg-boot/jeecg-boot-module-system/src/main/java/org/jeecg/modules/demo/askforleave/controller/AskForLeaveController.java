package org.jeecg.modules.demo.askforleave.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.askforleave.entity.AskForLeave;
import org.jeecg.modules.demo.askforleave.service.IAskForLeaveService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 请假发起表
 * @Author: jeecg-boot
 * @Date:   2020-04-20
 * @Version: V1.0
 */
@Api(tags="请假发起表")
@RestController
@RequestMapping("/askforleave/askForLeave")
@Slf4j
public class AskForLeaveController extends JeecgController<AskForLeave, IAskForLeaveService> {
	@Autowired
	private IAskForLeaveService askForLeaveService;
	
	/**
	 * 分页列表查询
	 *
	 * @param askForLeave
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "请假发起表-分页列表查询")
	@ApiOperation(value="请假发起表-分页列表查询", notes="请假发起表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AskForLeave askForLeave,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AskForLeave> queryWrapper = QueryGenerator.initQueryWrapper(askForLeave, req.getParameterMap());
		Page<AskForLeave> page = new Page<AskForLeave>(pageNo, pageSize);
		IPage<AskForLeave> pageList = askForLeaveService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param askForLeave
	 * @return
	 */
	@AutoLog(value = "请假发起表-添加")
	@ApiOperation(value="请假发起表-添加", notes="请假发起表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AskForLeave askForLeave) {
		askForLeaveService.save(askForLeave);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param askForLeave
	 * @return
	 */
	@AutoLog(value = "请假发起表-编辑")
	@ApiOperation(value="请假发起表-编辑", notes="请假发起表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AskForLeave askForLeave) {
		askForLeaveService.updateById(askForLeave);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "请假发起表-通过id删除")
	@ApiOperation(value="请假发起表-通过id删除", notes="请假发起表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		askForLeaveService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "请假发起表-批量删除")
	@ApiOperation(value="请假发起表-批量删除", notes="请假发起表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.askForLeaveService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "请假发起表-通过id查询")
	@ApiOperation(value="请假发起表-通过id查询", notes="请假发起表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		AskForLeave askForLeave = askForLeaveService.getById(id);
		if(askForLeave==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(askForLeave);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param askForLeave
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AskForLeave askForLeave) {
        return super.exportXls(request, askForLeave, AskForLeave.class, "请假发起表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, AskForLeave.class);
    }

}
