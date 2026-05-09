package com.springboot.iboribe.domain.codef.mapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.springboot.iboribe.domain.codef.dto.request.CodefChildList2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefChildRegister2WayRequest;
import com.springboot.iboribe.domain.codef.dto.request.CodefTreatment2WayRequest;

@Component
public class CodefRequestBodyMapper {

  public Map<String, Object> toChildRegister2WayBody(CodefChildRegister2WayRequest request) {
    Map<String, Object> body = new HashMap<>();

    body.put("organization", request.getOrganization());
    body.put("loginType", request.getLoginType());
    body.put("loginTypeLevel", request.getLoginTypeLevel());
    body.put("userName", request.getUserName());
    body.put("identity", request.getIdentity());
    body.put("phoneNo", request.getPhoneNo());
    body.put("telecom", request.getTelecom());
    body.put("id", request.getId());
    body.put("simpleAuth", request.getSimpleAuth());
    body.put("childName", request.getChildName());
    body.put("is2Way", true);
    body.put("twoWayInfo", request.getTwoWayInfo());

    return body;
  }

  public Map<String, Object> toChildList2WayBody(CodefChildList2WayRequest request) {
    Map<String, Object> body = new HashMap<>();

    body.put("organization", request.getOrganization());
    body.put("loginType", request.getLoginType());
    body.put("loginTypeLevel", request.getLoginTypeLevel());
    body.put("userName", request.getUserName());
    body.put("identity", request.getIdentity());
    body.put("phoneNo", request.getPhoneNo());
    body.put("telecom", request.getTelecom());
    body.put("id", request.getId());
    body.put("simpleAuth", request.getSimpleAuth());
    body.put("is2Way", true);
    body.put("twoWayInfo", request.getTwoWayInfo());

    return body;
  }

  public Map<String, Object> toTreatment2WayBody(CodefTreatment2WayRequest request) {
    Map<String, Object> body = new HashMap<>();

    body.put("organization", request.getOrganization());
    body.put("loginType", request.getLoginType());
    body.put("loginTypeLevel", request.getLoginTypeLevel());
    body.put("userName", request.getUserName());
    body.put("identity", request.getIdentity());
    body.put("phoneNo", request.getPhoneNo());
    body.put("telecom", request.getTelecom());
    body.put("id", request.getId());
    body.put("startDate", request.getStartDate());
    body.put("endDate", request.getEndDate());
    body.put("type", request.getType());
    body.put("drugImageYN", request.getDrugImageYN());
    body.put("medicationDirectionYN", request.getMedicationDirectionYN());
    body.put("detailYN", request.getDetailYN());
    body.put("timeOut", request.getTimeOut());
    body.put("simpleAuth", request.getSimpleAuth());
    body.put("secureNo", request.getSecureNo());
    body.put("secureNoRefresh", request.getSecureNoRefresh());
    body.put("is2Way", true);
    body.put("twoWayInfo", request.getTwoWayInfo());

    return body;
  }
}
