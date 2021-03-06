package com.redhat.reportengine.server.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.quartz.SchedulerException;

import com.redhat.reportengine.agent.rest.mapper.AgentDetails;
import com.redhat.reportengine.server.actions.ServerActions;
import com.redhat.reportengine.server.cache.ServerSettings;
import com.redhat.reportengine.server.dbdata.JobSchedulerTable;
import com.redhat.reportengine.server.dbdata.ServerTable;
import com.redhat.reportengine.server.dbmap.JobScheduler;
import com.redhat.reportengine.server.dbmap.Server;
import com.redhat.reportengine.server.rest.mapper.ServerInfo;
/**
 * @author jkandasa@redhat.com (Jeeva Kandasamy)
 * Aug 03, 2013
 */
public class AgentApi {
	public ServerInfo getServerInfo(AgentDetails agentDetails) throws SQLException, UnknownHostException{
		Server server = getServer(agentDetails);
		if(server == null){
			return null;
		}
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.setServerId(server.getId());
		serverInfo.setReServerUdpPort(ServerSettings.getServerUdpPort());
		serverInfo.setServerAddress(InetAddress.getLocalHost());
		serverInfo.setServerName(ServerSettings.getEngineURL());
		return serverInfo;
	}
	
	public void register(AgentDetails agentDetails) throws SQLException, ParseException, SchedulerException{
		Server server = getServer(agentDetails);		
		if(server == null){
			server = new Server();
			server.setAgentPort(agentDetails.getRestPort());
			server.setHostIp(agentDetails.getIp());
			server.setName(agentDetails.getName());
			server.setUpdateInterval(30*60);
			new ServerActions().add(server);
		}else if(!server.getHostIp().equalsIgnoreCase(agentDetails.getIp())){
			server.setHostIp(agentDetails.getIp());
			new ServerActions().update(server);
		}
	}
	
	public ArrayList<JobScheduler> getAgentJobs(AgentDetails agentDetails) throws SQLException{
		ArrayList<JobScheduler> jobs = new JobSchedulerTable().getAgentJobs(agentDetails.getServerId());
		return jobs;
	}
	
	public Server getServer(AgentDetails agentDetails) throws SQLException{
		Server server = new ServerTable().getByName(agentDetails.getName());
		if(server == null){
			server = new ServerTable().getByHostIp(agentDetails.getIp());
			if(server == null){
				server = new ServerTable().getByHostIp(agentDetails.getHostName());
			}
		}
		return server;
	}
}
