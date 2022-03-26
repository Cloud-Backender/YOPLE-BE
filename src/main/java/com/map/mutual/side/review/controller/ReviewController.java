package com.map.mutual.side.review.controller;

import com.map.mutual.side.common.dto.ResponseJsonObject;
import com.map.mutual.side.common.enumerate.ApiStatusCode;
import com.map.mutual.side.common.exception.YOPLEServiceException;
import com.map.mutual.side.common.utils.YOPLEUtils;
import com.map.mutual.side.review.model.dto.ReviewDto;
import com.map.mutual.side.review.svc.ReviewService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * fileName       : ReivewController
 * author         : kimjaejung
 * createDate     : 2022/03/22
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/03/22        kimjaejung       최초 생성
 */
@RestController
@Log4j2
@RequestMapping(value = "/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseJsonObject> upload(@RequestPart MultipartFile file, @RequestParam String tempReview) throws IOException {

        try {
            System.out.println("파일 이름 : " + file.getOriginalFilename());
            System.out.println("파일 크기 : " + file.getSize());

            String dirPath = File.separator
                    +"tmp"
                    +File.separator
                    +"yople"
                    +File.separator
                    +tempReview;

            YOPLEUtils.createDirectories(dirPath);


            FileOutputStream fos = new FileOutputStream(dirPath
                    +File.separator
                    +file.getOriginalFilename());

            InputStream is  = file.getInputStream();


            int readCount = 0;
            byte[] buffer = new byte[1024];

            while ((readCount = is.read(buffer)) != -1) {
                //  파일에서 가져온 fileInputStream을 설정한 크기 (1024byte) 만큼 읽고

                fos.write(buffer, 0, readCount);
                // 위에서 생성한 fileOutputStream 객체에 출력하기를 반복한다
            }

        } catch (YOPLEServiceException e) {
            throw e;
        }
        return new ResponseEntity<>(ResponseJsonObject.withStatusCode(ApiStatusCode.OK), HttpStatus.OK);
    }

    @PostMapping({"/createReview", "/updateReview"})
    public ResponseEntity<ResponseJsonObject> createUpdateReview(@RequestBody ReviewDto reviewDto) {
        try {
            reviewService.createUpdateReview(reviewDto);
        } catch (YOPLEServiceException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

        return new ResponseEntity<>(ResponseJsonObject.withStatusCode(ApiStatusCode.OK), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseJsonObject> getReview(@RequestParam Long reviewId) {
        ResponseJsonObject responseJsonObject = null;

        try {
            ReviewDto reviewDto = reviewService.getReview(reviewId);
            responseJsonObject.setData(reviewDto);

        } catch (YOPLEServiceException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }

        return new ResponseEntity<>(responseJsonObject, HttpStatus.OK);
    }

}
