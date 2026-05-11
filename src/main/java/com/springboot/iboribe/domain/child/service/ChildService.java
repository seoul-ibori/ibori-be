package com.springboot.iboribe.domain.child.service;

import java.util.List;

import com.springboot.iboribe.domain.child.dto.request.ChildUpdateRequest;
import com.springboot.iboribe.domain.child.dto.response.ChildResponse;
import com.springboot.iboribe.domain.child.dto.response.ChildUpdateResponse;

public interface ChildService {

  List<ChildResponse> getChildren(Long userId);

  ChildUpdateResponse updateChild(Long childId, ChildUpdateRequest request);

  void deleteChild(Long loginUserId, Long childId);
}
