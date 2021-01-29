package com.zhang.conf;//package com.zhang.upload.video.security.config.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhang.util.BodyReaderHttpServletRequestWrapper;
import com.zhang.util.ReqDedupHelper;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    StringRedisTemplate stringRedisTemplate; // 字符串Redis实例

    /**
     * 存放Token的Header Key
     */
    public static final String HEADER_STRING = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader(HEADER_STRING);
        if (null != token) {
            OutputStream os = null;
            try {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(username);
                    JwtUser userDetails = RedisUserUtil.getUser(stringRedisTemplate, token);
                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }

                    // 验证指定路径的请求重复性   比如添加或者修改操作
                    String requestURI = request.getRequestURI();
                    List<String> pathList = new ArrayList<>();
                    for (ReqDedupHelper.PathEnum pathEnum : ReqDedupHelper.PathEnum.values()) {
                        String path = pathEnum.getPath();
                        pathList.add(path);
                    }
                    if (pathList.contains(requestURI)) {
                        // 防止流读取一次后就没有了, 所以需要将流继续写出去
                        request = new BodyReaderHttpServletRequestWrapper(request);
                        byte[] body = ((BodyReaderHttpServletRequestWrapper) request).getBody();
                        String paramJson = new String(body, Charset.forName("UTF-8"));
                        if (ReqDedupHelper.verifyIsDedupReq(paramJson, userDetails.getId(), requestURI, stringRedisTemplate)) {
                            os = response.getOutputStream();
                            os.write("请勿重复操作".getBytes());
                            os.flush();
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

}
