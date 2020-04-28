package org.jeecg.modules.demo.studentattendance.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.signin.entity.SignInStudent;
import org.jeecg.modules.demo.signin.service.ISignInStudentService;
import org.jeecg.modules.demo.studentattendance.entity.StudentAttendance;
import org.jeecg.modules.demo.studentattendance.service.IStudentAttendanceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.util.SysInfoUtils;
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
 * @Description: 学生出勤统计
 * @Author: jeecg-boot
 * @Date:   2020-04-27
 * @Version: V1.0
 */
@Api(tags="学生出勤统计")
@RestController
@RequestMapping("/studentattendance/studentAttendance")
@Slf4j
@RequiredArgsConstructor
public class StudentAttendanceController extends JeecgController<StudentAttendance, IStudentAttendanceService> {

	private final IStudentAttendanceService studentAttendanceService;

	private final SysInfoUtils sysInfoUtil;

	private final ISysUserDepartService sysUserDepartService;

	private final ISignInStudentService signInStudentService;
	/**
	 * 分页列表查询
	 *
	 * @param studentAttendance
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "学生出勤统计-分页列表查询")
	@ApiOperation(value="学生出勤统计-分页列表查询", notes="学生出勤统计-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StudentAttendance studentAttendance,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		QueryWrapper<SysUserDepart> sysUserDepartQueryWrapper = new QueryWrapper<>();
        sysUserDepartQueryWrapper.eq("user_id", sysUser.getId()).last("limit 1");
        SysUserDepart sysUserDepart = sysUserDepartService.getOne(sysUserDepartQueryWrapper);
		studentAttendanceService.remove(null);
		List<SysUser> students = sysInfoUtil.getStudentsByCurrentTeacher(sysUserDepart.getDepId());
		if (ObjectUtil.isNotEmpty(students)) {
			for (SysUser student : students) {
				int askForLeave = 0;
				int late = 0;
				Double attendance = 0D;
				StudentAttendance studentAttendance1 = new StudentAttendance();
				QueryWrapper<SignInStudent> signInStudentQueryWrapper = new QueryWrapper<>();
				signInStudentQueryWrapper.eq("status", 1).eq("student_code", student.getUsername());
				QueryWrapper<SignInStudent> signInStudentQueryWrapper3 = new QueryWrapper<>();
				signInStudentQueryWrapper3.eq("student_code", student.getUsername());
				if (signInStudentService.list(signInStudentQueryWrapper3).size() != 0) {
					attendance = NumberUtil.div(signInStudentService.list(signInStudentQueryWrapper).size()
							, signInStudentService.list(signInStudentQueryWrapper3).size(), 2)*100;
				}
				QueryWrapper<SignInStudent> signInStudentQueryWrapper1 = new QueryWrapper<>();
				signInStudentQueryWrapper1.eq("status", 3).eq("student_code", student.getUsername());
				late = signInStudentService.list(signInStudentQueryWrapper1).size();
				QueryWrapper<SignInStudent> signInStudentQueryWrapper2 = new QueryWrapper<>();
				signInStudentQueryWrapper2.eq("status", 2).eq("student_code", student.getUsername());
				askForLeave = signInStudentService.list(signInStudentQueryWrapper2).size();
				studentAttendance1.setStudentCode(student.getUsername());
				studentAttendance1.setStudentName(student.getRealname());
				studentAttendance1.setAskforleave(askForLeave);
				studentAttendance1.setLate(late);
				studentAttendance1.setAttendance(attendance);
				studentAttendanceService.save(studentAttendance1);
			}
		}
		QueryWrapper<StudentAttendance> queryWrapper = QueryGenerator.initQueryWrapper(studentAttendance, req.getParameterMap());
		Page<StudentAttendance> page = new Page<StudentAttendance>(pageNo, pageSize);
		IPage<StudentAttendance> pageList = studentAttendanceService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param studentAttendance
	 * @return
	 */
	@AutoLog(value = "学生出勤统计-添加")
	@ApiOperation(value="学生出勤统计-添加", notes="学生出勤统计-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody StudentAttendance studentAttendance) {
		studentAttendanceService.save(studentAttendance);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param studentAttendance
	 * @return
	 */
	@AutoLog(value = "学生出勤统计-编辑")
	@ApiOperation(value="学生出勤统计-编辑", notes="学生出勤统计-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StudentAttendance studentAttendance) {
		studentAttendanceService.updateById(studentAttendance);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学生出勤统计-通过id删除")
	@ApiOperation(value="学生出勤统计-通过id删除", notes="学生出勤统计-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		studentAttendanceService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "学生出勤统计-批量删除")
	@ApiOperation(value="学生出勤统计-批量删除", notes="学生出勤统计-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.studentAttendanceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学生出勤统计-通过id查询")
	@ApiOperation(value="学生出勤统计-通过id查询", notes="学生出勤统计-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		StudentAttendance studentAttendance = studentAttendanceService.getById(id);
		if(studentAttendance==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(studentAttendance);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param studentAttendance
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StudentAttendance studentAttendance) {
        return super.exportXls(request, studentAttendance, StudentAttendance.class, "学生出勤统计");
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
        return super.importExcel(request, response, StudentAttendance.class);
    }

}
