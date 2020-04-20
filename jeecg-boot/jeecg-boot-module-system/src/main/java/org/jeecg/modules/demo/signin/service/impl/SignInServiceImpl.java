package org.jeecg.modules.demo.signin.service.impl;

import org.jeecg.modules.demo.signin.entity.SignIn;
import org.jeecg.modules.demo.signin.entity.SignInStudent;
import org.jeecg.modules.demo.signin.mapper.SignInStudentMapper;
import org.jeecg.modules.demo.signin.mapper.SignInMapper;
import org.jeecg.modules.demo.signin.service.ISignInService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 签到表
 * @Author: jeecg-boot
 * @Date:   2020-04-20
 * @Version: V1.0
 */
@Service
public class SignInServiceImpl extends ServiceImpl<SignInMapper, SignIn> implements ISignInService {

	@Autowired
	private SignInMapper signInMapper;
	@Autowired
	private SignInStudentMapper signInStudentMapper;
	
	@Override
	@Transactional
	public void delMain(String id) {
		signInStudentMapper.deleteByMainId(id);
		signInMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			signInStudentMapper.deleteByMainId(id.toString());
			signInMapper.deleteById(id);
		}
	}
	
}
