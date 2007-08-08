/*
 * ALMA - Atacama Large Millimiter Array (c) European Southern Observatory,
 * 2002 Copyright by ESO (in the framework of the ALMA collaboration), All
 * rights reserved
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alma.DATABASE_MODULE.DataBaseImpl;
import java.util.logging.Logger;

import org.omg.CORBA.DoubleHolder;
import org.omg.CORBA.IntHolder;

import alma.ACS.ComponentStates;
import alma.acs.component.ComponentLifecycle;
import alma.acs.container.ContainerServices;
import alma.DATABASE_MODULE.DataBaseOperations;
import alma.TYPES.*;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * A very simple component that does not make use of 
 * {@link alma.acs.component.ComponentImplBase}.
 * 
 * Javadoc comments have been removed to keep the
 * listing for the tutorial shorter.
 * 
 * @author mmsalgado vgonzalez nsaez
 */
public class DataBaseImpl implements ComponentLifecycle, DataBaseOperations
{
	private ContainerServices m_containerServices;
	private Logger m_logger;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	
	public void initialize(ContainerServices containerServices) {
		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();
		m_logger.info("initialize() called...");
	}
    
	public void execute() {
		m_logger.info("execute() called...");
	}
    
	public void cleanUp() {
		m_logger.info("cleanUp() called..., nothing to clean up.");
	}
    
	public void aboutToAbort() {
		cleanUp();
		m_logger.info("managed to abort...");
	}
	
	/////////////////////////////////////////////////////////////
	// Implementation of ACSComponent
	/////////////////////////////////////////////////////////////
	
	
	public ComponentStates componentState() {
		return m_containerServices.getComponentStateManager().getCurrentState();
	}
	public String name() {
		return m_containerServices.getName();
	}
	
	/////////////////////////////////////////////////////////////
	// Implementation of DataBaseOperations
	/////////////////////////////////////////////////////////////
      
	public int storeProposal(Target[] targetList){
		m_logger.fine("storeProposal is called");
		String path = new String(System.getenv("HOME")+"/tmp/data/");         
		File file = new File(path,"lastPid");
		Integer lastPid=0;
		try {

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			lastPid = new Integer(br.readLine());
			m_logger.finer("Last proposal id " + lastPid);
			br.close();
			fr.close();

		} catch (IOException e) {
		}

		lastPid++;    	
		File newPropDir = new File(path,lastPid.toString());
		newPropDir.mkdirs();

		File newPropStat = new File(newPropDir,"status");
		try {
			newPropStat.createNewFile();
			FileWriter fw = new FileWriter(newPropStat);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("0");
			bw.newLine();

			bw.close();
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (int j=0; j < targetList.length; j++)
		{
			int expTime = targetList[j].expTime;
			int tid = targetList[j].tid;
			Position newPos = targetList[j].coordinates;
			Double az = newPos.az;
			Double el = newPos.el;

			File newTargDir = new File(newPropDir,new Integer(tid).toString());
			newTargDir.mkdir();

			File targetText = new File(newTargDir,"target.txt");
			try {
				targetText.createNewFile();
				FileWriter fw = new FileWriter(targetText);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(az.toString());
				bw.newLine();

				bw.write(el.toString());
				bw.newLine();

				bw.write(new Integer(expTime).toString());
				bw.newLine();

				bw.close();
				fw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

			file = new File(path,"lastPid");

			try {	       	 
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(lastPid.toString());				
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		m_logger.finer("storeProposal returning successful !");
		return lastPid.intValue();
	}
      
	public int[][] getProposalObservations(int pid){

		int [] [] totalImages = new int[1][1];
		
		m_logger.finer("getProposalObservations called");
		
		if (getProposalStatus( pid) != 2) {
		   m_logger.info("This proposal isn't ready");
		} else {
			String path = new String(System.getenv("HOME")+"/tmp/data/");         

			File file = new File(path + pid);
			FileFilter fileFilter = new FileFilter() {
				public boolean accept(File file2) {
					return file2.isDirectory();
				}
			};
			File[] targets = file.listFiles(fileFilter);

			int images[];
			totalImages = new int [targets.length][3000];
			for (int j=0; j < targets.length; j++){
				file = new File(targets[j], "image.dat");

				try {
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					String az_s = br.readLine();

					String [] az_sList=az_s.split(" ");


					br.close();
					fr.close();

					images=new int[3000];
					for (int k = 0; k < az_sList.length;k++)
					{
						images[k] = new Integer(az_sList[k]).intValue();
					}
					totalImages [j] = images;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		m_logger.finer("getProposalObservations returning successful! ! ");
		return totalImages;
	}

	public Proposal[] getProposals(){

		m_logger.finer("getProposals called ");
		String path = new String(System.getenv("HOME")+"/tmp/data/");
		File file = new File(path);


		FilenameFilter fileFilter2 = new FilenameFilter() {
			public boolean accept(File file2,String name) {
				return !name.equals("lastPid");            }
		};
		String[] proposals = file.list(fileFilter2);

		
		int totalList=0;
		for (int k=0; k < proposals.length; k++){
			if (getProposalStatus( new Integer(proposals[k])) == 0) {
				totalList++;
			}
		}

		Proposal [] propList = new Proposal[totalList];

		int proposalsCounter=0;

		for (int i=0; i < proposals.length; i++){
			if (getProposalStatus( new Integer(proposals[i])) == 0) {
				file = new File(path +  proposals[i]);
				FileFilter fileFilter = new FileFilter() {
					public boolean accept(File file2) {
						return file2.isDirectory();
					}
				};
				File[] targets = file.listFiles(fileFilter);

				Target [] targetDescs = new Target[targets.length];

				for (int j=0; j < targets.length; j++){
					file = new File(targets[j], "target.txt");

					try {
						FileReader fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);
						String az_s = br.readLine();
						String el_s = br.readLine();
						String expT_s = br.readLine();


						Position newpos = new Position(new Double(az_s).doubleValue(),new Double(el_s).doubleValue());
						targetDescs[j] = new Target (new Integer(targets[j].getName()),newpos, new Integer(expT_s));

						br.close();
						fr.close();


					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				propList[proposalsCounter] = new Proposal(new Integer(proposals[i].toString()),targetDescs,0);
				proposalsCounter++;


			} else {
				// MENSAJE SIN PROPOSALS QUEUED
				m_logger.info("No proposals queued !");
			}

		}

		m_logger.finer("getProposals return successful !!");
		return propList;
	}

	public void storeImage(int pid, int tid, int[] image) {
		
		Integer spid = new Integer(pid);
		Integer stid = new Integer(tid);     
		String path = new String(System.getenv("HOME")+"/tmp/data/"+spid.toString()+"/"+stid.toString()+"/image.png");          
		
		m_logger.finer("storeImage called with pid: " + new Integer(pid).toString());
		m_logger.finer("Image length: " + image.length);
		
	
		m_logger.finer(" new image : " + path);
		
		File file= new File(path);
		BufferedImage img = new BufferedImage(640,480,BufferedImage.TYPE_BYTE_INDEXED);
		for(int i = 0;i<640;i++){
			for(int j=0;j<480;j++){
				if( 640*i+j < image.length )
					img.setRGB(j, i, image[640*i+j+1]);
			}
		}


		try {
			if (file.createNewFile()){
		        m_logger.finer("Image file created");
				
			ImageIO.write(img, "png",file);	
				
		        m_logger.finer("Image file written");
				
			}else{
				m_logger.info("Image already exist!");
			}
		} catch (IOException e) {
		      e.printStackTrace();
		      m_logger.finer("failed to created image !");

		}
       	m_logger.finer("storeImage successful");

	return;
		
	}	


	public void setProposalStatus(int pid, int status){
		Integer spid = new Integer(pid);
		Integer stat = new Integer(status);
		String path = new String(System.getenv("HOME")+"/tmp/data/"+spid.toString()+"/status");         
		File file = new File(path);
		m_logger.finer("setProposalStatus called");
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter br = new BufferedWriter(fw);
			br.write(stat.toString());
			br.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}    

		m_logger.finer("setProposalStatus successful");
	}

	public int getProposalStatus(int pid){

		m_logger.finer("getProposalStatus called");
		Integer spid = new Integer(pid);
		String path = new String(System.getenv("HOME")+"/tmp/data/"+spid.toString()+"/status");         
		String status=null;
		File file = new File(path);
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			status = br.readLine();
			br.close();
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		Integer status1 = new Integer(status);
		m_logger.finer("getProposalStatus successful");
		return status1;
	}
	

	

}

