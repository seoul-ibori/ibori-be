package com.springboot.iboribe.domain.codef.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.springboot.iboribe.domain.codef.dto.response.CodefChildListResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefChildRegisterResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefMedicalRecordResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefMedicationResponse;
import com.springboot.iboribe.domain.codef.dto.response.CodefTreatmentResponse;
import com.springboot.iboribe.domain.codef.exception.CodefErrorCode;
import com.springboot.iboribe.global.exception.CustomException;

@Component
public class CodefResponseMapper {

  public CodefChildRegisterResponse toChildRegisterResponse(Map<String, Object> response) {
    Object resultObject = response.get("result");

    if (!(resultObject instanceof Map<?, ?> result)) {
      throw new CustomException(CodefErrorCode.CODEF_INVALID_RESPONSE);
    }

    Object dataObject = response.get("data");

    Map<?, ?> data = null;
    if (dataObject instanceof Map<?, ?> dataMap) {
      data = dataMap;
    }

    Boolean continue2Way = data != null ? (Boolean) data.get("continue2Way") : null;
    Map<String, Object> twoWayInfo = extractTwoWayInfo(data, continue2Way);

    return CodefChildRegisterResponse.builder()
        .resultCode(toNullableString(result.get("code")))
        .resultMessage(toNullableString(result.get("message")))
        .continue2Way(continue2Way)
        .method(data != null ? toNullableString(data.get("method")) : null)
        .twoWayInfo(twoWayInfo)
        .resRegistrationStatus(
            data != null ? toNullableString(data.get("resRegistrationStatus")) : null)
        .resResultDesc(data != null ? toNullableString(data.get("resResultDesc")) : null)
        .rawData(response)
        .build();
  }

  public CodefChildListResponse toChildListResponse(Map<String, Object> response) {
    Object resultObject = response.get("result");

    if (!(resultObject instanceof Map<?, ?> result)) {
      throw new CustomException(CodefErrorCode.CODEF_INVALID_RESPONSE);
    }

    Object dataObject = response.get("data");

    Map<?, ?> data = null;
    if (dataObject instanceof Map<?, ?> dataMap) {
      data = dataMap;
    }

    Boolean continue2Way = data != null ? (Boolean) data.get("continue2Way") : null;
    Map<String, Object> twoWayInfo = extractTwoWayInfo(data, continue2Way);
    List<Map<String, Object>> children = extractChildren(dataObject, continue2Way);

    return CodefChildListResponse.builder()
        .resultCode(toNullableString(result.get("code")))
        .resultMessage(toNullableString(result.get("message")))
        .continue2Way(continue2Way)
        .method(data != null ? toNullableString(data.get("method")) : null)
        .twoWayInfo(twoWayInfo)
        .children(children)
        .rawData(response)
        .build();
  }

  public CodefTreatmentResponse toTreatmentResponse(Map<String, Object> response) {
    Object resultObject = response.get("result");

    if (!(resultObject instanceof Map<?, ?> result)) {
      throw new CustomException(CodefErrorCode.CODEF_INVALID_RESPONSE);
    }

    Object dataObject = response.get("data");

    Map<?, ?> data = null;
    if (dataObject instanceof Map<?, ?> dataMap) {
      data = dataMap;
    }

    Boolean continue2Way = data != null ? (Boolean) data.get("continue2Way") : null;
    Map<String, Object> twoWayInfo = extractTwoWayInfo(data, continue2Way);
    List<CodefMedicalRecordResponse> medicalRecords =
        extractMedicalRecords(dataObject, continue2Way);

    return CodefTreatmentResponse.builder()
        .resultCode(toNullableString(result.get("code")))
        .resultMessage(toNullableString(result.get("message")))
        .continue2Way(continue2Way)
        .method(data != null ? toNullableString(data.get("method")) : null)
        .twoWayInfo(twoWayInfo)
        .medicalRecords(medicalRecords)
        .rawData(response)
        .build();
  }

  private Map<String, Object> extractTwoWayInfo(Map<?, ?> data, Boolean continue2Way) {
    if (data == null || !Boolean.TRUE.equals(continue2Way)) {
      return null;
    }

    Map<String, Object> twoWayInfo = new HashMap<>();
    twoWayInfo.put("jobIndex", data.get("jobIndex"));
    twoWayInfo.put("threadIndex", data.get("threadIndex"));
    twoWayInfo.put("jti", data.get("jti"));
    twoWayInfo.put("twoWayTimestamp", data.get("twoWayTimestamp"));

    return twoWayInfo;
  }

  @SuppressWarnings("unchecked")
  private List<Map<String, Object>> extractChildren(Object dataObject, Boolean continue2Way) {
    if (Boolean.TRUE.equals(continue2Way) || dataObject == null) {
      return null;
    }

    List<Map<String, Object>> children = new ArrayList<>();

    if (dataObject instanceof List<?> list) {
      for (Object item : list) {
        if (item instanceof Map<?, ?> child) {
          children.add((Map<String, Object>) child);
        }
      }
      return children;
    }

    if (dataObject instanceof Map<?, ?> child) {
      children.add((Map<String, Object>) child);
    }

    return children;
  }

  @SuppressWarnings("unchecked")
  private List<CodefMedicalRecordResponse> extractMedicalRecords(
      Object dataObject, Boolean continue2Way) {

    if (Boolean.TRUE.equals(continue2Way) || dataObject == null) {
      return null;
    }

    List<CodefMedicalRecordResponse> medicalRecords = new ArrayList<>();

    if (dataObject instanceof List<?> list) {
      for (Object item : list) {
        if (item instanceof Map<?, ?> record) {
          medicalRecords.add(toMedicalRecord((Map<String, Object>) record));
        }
      }
      return medicalRecords;
    }

    if (dataObject instanceof Map<?, ?> record) {
      medicalRecords.add(toMedicalRecord((Map<String, Object>) record));
    }

    return medicalRecords;
  }

  @SuppressWarnings("unchecked")
  private CodefMedicalRecordResponse toMedicalRecord(Map<String, Object> record) {
    Object mediDetailObject = record.get("resMediDetailList");

    List<CodefMedicationResponse> medications = new ArrayList<>();

    if (mediDetailObject instanceof List<?> list) {
      for (Object item : list) {
        if (item instanceof Map<?, ?> medication) {
          medications.add(toMedication((Map<String, Object>) medication));
        }
      }
    }

    return CodefMedicalRecordResponse.builder()
        .subjectName(toNullableString(record.get("resType")))
        .hospitalName(toNullableString(record.get("resHospitalName")))
        .treatDate(toNullableString(record.get("resTreatStartDate")))
        .treatType(toNullableString(record.get("resTreatType")))
        .medications(medications)
        .build();
  }

  private CodefMedicationResponse toMedication(Map<String, Object> medication) {
    return CodefMedicationResponse.builder()
        .treatDate(toNullableString(medication.get("resTreatDate")))
        .treatTypeDetail(toNullableString(medication.get("resTreatTypeDet")))
        .drugName(toNullableString(medication.get("resPrescribeDrugName")))
        .drugEffect(toNullableString(medication.get("resPrescribeDrugEffect")))
        .prescribeDays(toNullableString(medication.get("resPrescribeDays")))
        .build();
  }

  private String toNullableString(Object value) {
    if (value == null) {
      return null;
    }

    String stringValue = String.valueOf(value);

    return stringValue.isBlank() ? null : stringValue;
  }
}
