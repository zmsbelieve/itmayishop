package com.itmyshop.fegin;

import com.itmayi.api.service.MemberService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(value = "member")
@Component
public interface MemberServiceFegin extends MemberService {
}
