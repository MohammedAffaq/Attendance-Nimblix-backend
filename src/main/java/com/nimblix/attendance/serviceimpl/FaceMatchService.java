package com.nimblix.attendance.serviceimpl;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class FaceMatchService {
    public boolean match(File saved, File live) {
        return true; // Replace with real face AI later
    }
}
