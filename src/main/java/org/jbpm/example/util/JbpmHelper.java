package org.jbpm.example.util;

import java.util.List;

import org.jbpm.process.audit.AuditLogService;
import org.jbpm.process.audit.ProcessInstanceLog;

public class JbpmHelper {
	
	private static String[] statuses = {"Pending", "Active", "Completed", "Aborted", "Suspended"};
	
	public static ProcessInstanceLog getLastProcessInstanceLog(String processId, AuditLogService auditService) {
		List<ProcessInstanceLog> pil = auditService.findProcessInstances(processId);
		if (pil.size() == 0) {
			return null;
		}
		return pil.get(pil.size() - 1);
	}
	
	public static String getProcessStatus(String processId, AuditLogService auditService) {
		ProcessInstanceLog log = JbpmHelper.getLastProcessInstanceLog(processId, auditService);
		if (log == null) {
			return "Unknown";
		}
		return getProcessStatus(log.getStatus());
	}
	
	public static String getProcessStatus(Integer status) {
		return statuses[status];
	}
	
}
