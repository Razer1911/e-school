package org.jeecg.modules.demo.signin.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.signin.entity.SignInStudent;
import org.jeecg.modules.demo.signin.entity.SignIn;
import org.jeecg.modules.demo.signin.service.ISignInService;
import org.jeecg.modules.demo.signin.service.ISignInStudentService;


 /**
 * @Description: 签到表
 * @Author: jeecg-boot
 * @Date:   2020-04-20
 * @Version: V1.0
 */
@RestController
@RequestMapping("/signin/signIn")
@Slf4j
public class SignInController extends JeecgController<SignIn, ISignInService> {

	@Autowired
	private ISignInService signInService;

	@Autowired
	private ISignInStudentService signInStudentService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param signIn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SignIn signIn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SignIn> queryWrapper = QueryGenerator.initQueryWrapper(signIn, req.getParameterMap());
		Page<SignIn> page = new Page<SignIn>(pageNo, pageSize);
		IPage<SignIn> pageList = signInService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
     *   添加
     * @param signIn
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SignIn signIn) {
        signInService.save(signIn);
        return Result.ok("添加成功！");
    }

    /**
     *  编辑
     * @param signIn
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SignIn signIn) {
        signInService.updateById(signIn);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        signInService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.signInService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SignIn signIn) {
        return super.exportXls(request, signIn, SignIn.class, "签到表");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SignIn.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-学生附表-begin----------------------------------------------*/
	/**
	 * 查询子表信息 会传入主表ID
	 * @return
	 */
	@GetMapping(value = "/listSignInStudentByMainId")
    public Result<?> listSignInStudentByMainId(SignInStudent signInStudent,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<SignInStudent> queryWrapper = QueryGenerator.initQueryWrapper(signInStudent, req.getParameterMap());
        Page<SignInStudent> page = new Page<SignInStudent>(pageNo, pageSize);
        IPage<SignInStudent> pageList = signInStudentService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

	/**
	 * 添加
	 * @param signInStudent
	 * @return
	 */
	@PostMapping(value = "/addSignInStudent")
	public Result<?> addSignInStudent(@RequestBody SignInStudent signInStudent) {
		signInStudentService.save(signInStudent);
		return Result.ok("添加成功！");
	}

    /**
	 * 编辑
	 * @param signInStudent
	 * @return
	 */
	@PutMapping(value = "/editSignInStudent")
	public Result<?> editSignInStudent(@RequestBody SignInStudent signInStudent) {
		signInStudentService.updateById(signInStudent);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/deleteSignInStudent")
	public Result<?> deleteSignInStudent(@RequestParam(name="id",required=true) String id) {
		signInStudentService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatchSignInStudent")
	public Result<?> deleteBatchSignInStudent(@RequestParam(name="ids",required=true) String ids) {
		this.signInStudentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

    /*--------------------------------子表处理-学生附表-end----------------------------------------------*/




}
