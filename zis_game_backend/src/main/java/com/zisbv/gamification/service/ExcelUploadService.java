package com.zisbv.gamification.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.zisbv.gamification.dto.AdminBulkUserUploadDto;
import com.zisbv.gamification.dto.SuperAdminBulkUserUploadDto;

public interface ExcelUploadService {

	// Admin
	public List<Map<String, String>> uploadBulkUsersForAdmin(MultipartFile file) throws Exception;

	public AdminBulkUserUploadDto validateUser(AdminBulkUserUploadDto data, int emailOccurance);

	public void saveData(AdminBulkUserUploadDto data);

	public ByteArrayInputStream adminErrorLogToExcelFile(List<AdminBulkUserUploadDto> exelUsers);

	// SuperAdmin
	public List<Map<String, String>> uploadBulkUsersForSuperAdmin(MultipartFile file) throws Exception;

	public SuperAdminBulkUserUploadDto validateUser(SuperAdminBulkUserUploadDto data, int emailOccurance);

	public void saveData(SuperAdminBulkUserUploadDto data);

	public ByteArrayInputStream superAdminErrorLogToExelFile(List<SuperAdminBulkUserUploadDto> exelUsers);

}
