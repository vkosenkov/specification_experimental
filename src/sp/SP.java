package sp;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;

import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.pse.plugin.Activator;

import reports.EnumBlockType;
import reports.ErrorList;
import sp.spblock.SPBlock;
import sp.spblock.SPBlockList;
import sp.xml.XmlBuilder;
import sp.xml.XmlBuilderConfiguration;

public class SP
{
	public static enum FormField {
		FORMAT, ZONE, POSITION, ID, NAME, QUANTITY, REMARK
	};

	public static TCComponentBOMLine topBOMLine;
	public static TCComponentItem topBOMLineI;
	public static TCComponentItemRevision topBOMLineIR;
	public static TCComponentItemRevision topBOMLinePrevIR;
	public static TCComponentItemRevision spIR;

	public ProgressMonitorDialog progressMonitor;
	public StampData stampData;
	public Report report;
	public ErrorList errorList;

	public SP()
	{
		stampData = new StampData();
		report = new Report("My SP Report");
		report.blockList = new SPBlockList();
		initBlocks(report.blockList);
		errorList = new ErrorList();
	}
	
	public void init()
	{
		try{
			topBOMLine = Activator.getPSEService().getTopBOMLine();
			topBOMLineI = topBOMLine.getItem();
			topBOMLineIR = topBOMLine.getItemRevision();
		} catch (TCException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error while initializing");
		}
	}
	
	void initBlocks(SPBlockList blockList){
		blockList.add(new SPBlock(EnumBlockType.ASSEMBLIES));
		blockList.add(new SPBlock(EnumBlockType.COMPLEXES));
		blockList.add(new SPBlock(EnumBlockType.DETAILS));
		blockList.add(new SPBlock(EnumBlockType.DOCUMENTS));
		blockList.add(new SPBlock(EnumBlockType.KITS));
		blockList.add(new SPBlock(EnumBlockType.MATERIALS));
		blockList.add(new SPBlock(EnumBlockType.OTHERS));
		blockList.add(new SPBlock(EnumBlockType.STANDARDS));
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
		InputStream template = SP.class.getResourceAsStream("/pdf/OceanosSpecPDFTemplate.xsl");
		InputStream config = SP.class.getResourceAsStream("/pdf/OceanosSpecUserconfig.xsl");
		PDFBuilderConfiguration A4pdfBuilderconfiguration = new PDFBuilderConfiguration(template, config);

		report.configuration = A4pdfBuilderconfiguration;

		OceanosReportBuilder reportBuilder = new OceanosReportBuilder(report);
		reportBuilder.buildReport();
	}
	
	public void uploadReportFile()
	{
		ReportUploader uploader = new ReportUploader();
		uploader.addToTeamcenter();
	}

	public void initReportConfigurations()
	{

	}
}
