package com.zhang.conf;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class JwtUserDetailsService implements UserDetailsService {

//    @Value("${sbms.system-id}")
    private String sbmsSystemId;

//    @Autowired
//    private UserService userService;
//    @Autowired
//    private RoleService roleService;
//    @Autowired
//    private PrivilegeService privilegeService;


    @Override
    public UserDetails loadUserByUsername(String account) {

//        SysUser user =  userService.getByUserName(account);
//        if (user == null) {
//            //throw new NullPointerException("用户不存在！");
//            return null;
//        }
//
//        //List<Role> roles = roleService.findByuId(user.getId());
//        List<Privilege> privileges = privilegeService.findPriByuId(user.getId(),sbmsSystemId);
//
//        return createJwtUser(user, privileges);
        return null;


    }

//    public UserDetails createJwtUser(SysUser user, List<Privilege> privileges) {
//        List<GrantedAuthority> auths = new ArrayList<>();
//        String[] roles = user.getRoleName().split(",");
//
//        for(int i=0;i<roles.length;i++){
//            if(StringUtils.isNotBlank(roles[i])){
//                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roles[i]);
//                auths.add(grantedAuthority);
//            }
//        }
//
//        JwtUser jwtUser = new JwtUser();
//        jwtUser.setId(user.getId());
//        jwtUser.setUsername(user.getName());
//        jwtUser.setPassword(user.getPassword());
//        jwtUser.setAuthorities(auths);
//        jwtUser.setCreateTime(user.getCreateTime());
//        jwtUser.setOrgId(user.getOrgId());
//        jwtUser.setAccount(user.getAccount());
//        jwtUser.setStatus(user.getStatus());
//        jwtUser.setTelephone(user.getTelephone());
//        jwtUser.setRoleName(user.getRoleName());
////        jwtUser.setSchools(user.getSchools());
//        jwtUser.setEnabled(true);
//        return jwtUser;
//    }
}
