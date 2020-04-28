package org.jeecg.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserDepart;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SysInfoUtils {
    private final ISysUserService sysUserService;
    private final ISysUserRoleService sysUserRoleService;
    private final ISysUserDepartService sysUserDepartService;
    /**
     * 获取当前教师所在班级所有学生
     * @return
     */
    public List<SysUser> getStudentsByCurrentTeacher(String orgCode) {
        List<SysUser> resList = CollUtil.newArrayList();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        QueryWrapper<SysUserDepart> sysUserDepartQueryWrapper = new QueryWrapper<>();
//        sysUserDepartQueryWrapper.eq("user_id", sysUser.getId()).last("limit 1");
//        SysUserDepart sysUserDepart = sysUserDepartService.getOne(sysUserDepartQueryWrapper);
        QueryWrapper<SysUserDepart> sysUserDepartQueryWrapper1 = new QueryWrapper<>();
//        sysUserDepartQueryWrapper1.eq("dep_id", sysUserDepart.getDepId()).ne("user_id", sysUser.getId());
        List<String> teacherIds = CollUtil.newArrayList();
        QueryWrapper<SysUserRole> teacherRoleQueryWrapper = new QueryWrapper<>();
        teacherRoleQueryWrapper.eq("role_id", "1252430975869513729");
        List<SysUserRole> teacherList = sysUserRoleService.list(teacherRoleQueryWrapper);
        sysUserDepartQueryWrapper1.eq("dep_id", orgCode).ne("user_id", teacherList);
        List<SysUserDepart> sysUserDeparts = sysUserDepartService.list(sysUserDepartQueryWrapper1);
        List<SysUser> users = CollUtil.newArrayList();
        if (ObjectUtil.isNotNull(sysUserDeparts) && ObjectUtil.isNotEmpty(sysUserDeparts)) {
            for (SysUserDepart sysUserDepart1 : sysUserDeparts) {
                users.add(sysUserService.getById(sysUserDepart1.getUserId()));
            }
        }
        if (ObjectUtil.isNotNull(users) && ObjectUtil.isNotEmpty(users)) {
            for (SysUser user : users) {
                QueryWrapper<SysUserRole> sysUserRoleQueryWrapper = new QueryWrapper<>();
                sysUserRoleQueryWrapper.eq("user_id", user.getId());
                List<SysUserRole> sysUserRoleList = sysUserRoleService.list(sysUserRoleQueryWrapper);
                if (ObjectUtil.isNotNull(sysUserRoleList) && ObjectUtil.isNotEmpty(sysUserRoleList)) {
                    for (SysUserRole sysUserRole : sysUserRoleList) {
                        if (StrUtil.equals(sysUserRole.getRoleId(), "1252430912149647361")) {
                            resList.add(user);
                        }
                    }
                }
            }
        }
        return resList;
    }
}
