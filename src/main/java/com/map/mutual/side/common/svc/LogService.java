package com.map.mutual.side.common.svc;

import com.map.mutual.side.common.entity.ApiLog;
import com.map.mutual.side.common.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/** Class       : LogService (Service)
 *  Author      : 조 준 희
 *  Description : 로그 관련 서비스 객체  ( Interface의 필요성 아직 모르겠어서 없음. )
 *  History     : [2022-01-03] - Temp
 */
@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    // ApiLog 데이터 save
    public boolean InsertApiLog(ApiLog logData){
        try{
            ApiLog data = logRepository.save(logData);

            if(data != null)
                return true;
            else
                return false;

        }catch (Exception ex) {
            throw ex;
        }
}
}
