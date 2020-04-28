package org.jeecg.modules.demo.askforleave.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.askforleave.entity.AskForLeave;
import org.jeecg.modules.demo.askforleave.service.IAskForLeaveService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.demo.signin.entity.SignInStudent;
import org.jeecg.modules.demo.signin.service.ISignInService;
import org.jeecg.modules.demo.signin.service.ISignInStudentService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.service.ISysUserRoleService;
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
@RequiredArgsConstructor
public class AskForLeaveController extends JeecgController<AskForLeave, IAskForLeaveService> {

	private final IAskForLeaveService askForLeaveService;

	private final ISysUserRoleService sysUserRoleService;

	private final ISignInService signInService;

	private final ISignInStudentService signInStudentService;
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
	@PermissionData(pageComponent="modules/askforleave/AskForLeaveList")
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
	  * 分页列表查询
	  *
	  * @param askForLeave
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "请假审批表-分页列表查询")
	 @ApiOperation(value="请假审批表-分页列表查询", notes="请假审批表-分页列表查询")
	 @GetMapping(value = "/list1")
	 @PermissionData(pageComponent="modules/askforleave/AskForLeaveListOne")
	 public Result<?> queryPageList1(AskForLeave askForLeave,
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
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		askForLeave.setStudentCode(sysUser.getUsername());
		askForLeave.setStudentName(sysUser.getRealname());
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

	 /**
	  * 通过学生请假申请
	  * @return
	  */
	 @GetMapping(value = "/pass")
	 public Result<?> pass(@RequestParam(name = "ids", defaultValue = "1") String ids) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
	 	 List<String> idList = Arrays.asList(StrUtil.split(ids, StrUtil.COMMA));
	 	 if (ObjectUtil.isNotEmpty(idList)) {
	 	 	 for (String id : idList) {
	 	 	 	 if (StrUtil.isNotBlank(id)) {
					 AskForLeave askForLeave = askForLeaveService.getById(id);
					 askForLeave.setStatus(1);
					 askForLeave.setTeacherCode(sysUser.getUsername());
					 askForLeave.setTeacherName(sysUser.getRealname());
					 askForLeaveService.updateById(askForLeave);
				 }
			 }
		 }
		 return Result.ok("已审批通过");
	 }

	 /**
	  * 获取出勤率
	  * @return
	  */
	 @GetMapping(value = "/getAttendance")
	 public Result<?> getAttendance() {
		 int unprocessed = 0;
		 int askForLeave = 0;
		 int late = 0;
		 Double attendance = 0D;
		 Map<String, Number> resMap = CollUtil.newHashMap();
		 resMap.put("askForLeave", 0);
		 resMap.put("late", 0);
		 resMap.put("attendance", 0D);
		 resMap.put("unprocessed", 0);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 QueryWrapper<SysUserRole> sysUserRoleQueryWrapper = new QueryWrapper<>();
		 sysUserRoleQueryWrapper.eq("user_id", sysUser.getId());
		 List<SysUserRole> sysUserRoleList = sysUserRoleService.list(sysUserRoleQueryWrapper);
		 for (SysUserRole sysUserRole : sysUserRoleList) {
		 	//如果登录者是教师
		 	if (StrUtil.equals(sysUserRole.getRoleId(), "1252430975869513729") || StrUtil.equals(sysUserRole.getRoleId(), "f6817f48af4fb3af11b9e8bf182f618b")) {
				QueryWrapper<SignInStudent> signInStudentQueryWrapper = new QueryWrapper<>();
				signInStudentQueryWrapper.eq("status", 1);
				attendance = NumberUtil.div(signInStudentService.list(signInStudentQueryWrapper).size()
						, signInStudentService.list().size(), 2)*100;
				QueryWrapper<SignInStudent> signInStudentQueryWrapper1 = new QueryWrapper<>();
				signInStudentQueryWrapper1.eq("status", 3);
				late = signInStudentService.list(signInStudentQueryWrapper1).size();
				QueryWrapper<SignInStudent> signInStudentQueryWrapper2 = new QueryWrapper<>();
				signInStudentQueryWrapper2.eq("status", 2);
				askForLeave = signInStudentService.list(signInStudentQueryWrapper2).size();
				QueryWrapper<AskForLeave> askForLeaveQueryWrapper = new QueryWrapper<>();
				askForLeaveQueryWrapper.eq("status", 0);
				unprocessed = askForLeaveService.list(askForLeaveQueryWrapper).size();
				resMap.put("askForLeave", askForLeave);
				resMap.put("late", late);
				resMap.put("attendance", attendance);
				resMap.put("unprocessed", unprocessed);
			}
		 	//如果登录者是学生
			if (StrUtil.equals(sysUserRole.getRoleId(), "1252430912149647361")) {
				QueryWrapper<SignInStudent> signInStudentQueryWrapper = new QueryWrapper<>();
				signInStudentQueryWrapper.eq("status", 1).eq("student_code", sysUser.getUsername());
				QueryWrapper<SignInStudent> signInStudentQueryWrapper3 = new QueryWrapper<>();
				signInStudentQueryWrapper3.eq("student_code", sysUser.getUsername());
				attendance = NumberUtil.div(signInStudentService.list(signInStudentQueryWrapper).size()
						, signInStudentService.list(signInStudentQueryWrapper3).size(), 2)*100;
				QueryWrapper<SignInStudent> signInStudentQueryWrapper1 = new QueryWrapper<>();
				signInStudentQueryWrapper1.eq("status", 3).eq("student_code", sysUser.getUsername());
				late = signInStudentService.list(signInStudentQueryWrapper1).size();
				QueryWrapper<SignInStudent> signInStudentQueryWrapper2 = new QueryWrapper<>();
				signInStudentQueryWrapper2.eq("status", 2).eq("student_code", sysUser.getUsername());
				askForLeave = signInStudentService.list(signInStudentQueryWrapper2).size();
				resMap.put("askForLeave", askForLeave);
				resMap.put("late", late);
				resMap.put("attendance", attendance);
			}
		 }
		 return Result.ok(resMap);
	 }
}
