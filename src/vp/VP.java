package vp;

import java.io.File;
import java.io.InputStream;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;

import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.pse.plugin.Activator;

import reports.ErrorList;
import vp.Report;
import vp.vpblock.VPBlockList;
import vp.xml.XmlBuilder;
import vp.xml.XmlBuilderConfiguration;

public class VP
{
	static enum FormField {NUMBER, FORMAT, ZONE, ID, NAME, QTY, REMARK}

	public static TCComponentBOMLine topBOMLine;
	public static TCComponentItem topBOMLineI;
	public static TCComponentItemRevision topBOMLineIR;
	public static TCComponentItemRevision vpIR;
	
	public ProgressMonitorDialog progressMonitor;
	public StampData stampData;
	public Report report;
	public ErrorList errorList;
	
	public VP()
	{
		report = new Report("My VP Report");
		report.blockList = new VPBlockList();
	}
	
	public void init()
	{
		try {
			topBOMLine = Activator.getPSEService().getTopBOMLine();
			topBOMLineI = topBOMLine.getItem();
			topBOMLineIR = topBOMLine.getItemRevision();
		} catch (TCException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error while initializing");
		}
	}
	
	public void readData()
	{
		DataReader dataReader = new DataReader(this);
		dataReader.readData();
	}

	public void buildXmlFile()
	{
		XmlBuilderConfiguration A4xmlBuilderConfiguration = new XmlBuilderConfiguration(26, 32);
		A4xmlBuilderConfiguration.MaxWidthGlobalRemark = 474;

		XmlBuilder xmlBuilder = new XmlBuilder(A4xmlBuilderConfiguration);
		File data = xmlBuilder.buildXml();

		report.data = data;
	}

	public void buildReportFile()
	{
		InputStream template = VP.class.getResourceAsStream("/pdf/OceanosVPPDFTemplate.xsl");
		InputStream config = VP.class.getResourceAsStream("/pdf/OceanosVPUserconfig.xsl");
		PDFBuilderConfiguration A4pdfBuilderconfiguration = new PDFBuilderConfiguration(template, config);

		report.configuration = A4pdfBuilderconfiguration;

		OceanosReportBuilder reportBuilder = new OceanosReportBuilder(report);
	}
	
	public void uploadReportFile()
	{
		ReportUploader uploader = new ReportUploader();
		uploader.addToTeamcenter();
	}
}
