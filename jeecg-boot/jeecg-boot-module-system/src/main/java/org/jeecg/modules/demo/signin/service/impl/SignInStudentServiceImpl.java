package org.jeecg.modules.demo.signin.service.impl;

import org.jeecg.modules.demo.signin.entity.SignInStudent;
import org.jeecg.modules.demo.signin.mapper.SignInStudentMapper;
import org.jeecg.modules.demo.signin.service.ISignInStudentService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 学生附表
 * @Author: jeecg-boot
 * @Date:   2020-04-25
 * @Version: V1.0
 */
@Service
public class SignInStudentServiceImpl extends ServiceImpl<SignInStudentMapper, SignInStudent> implements ISignInStudentService {
	
	@Autowired
	private SignInStudentMapper signInStudentMapper;
	
	@Override
	public List<SignInStudent> selectByMainId(String mainId) {
		return signInStudentMapper.selectByMainId(mainId);
	}
}
