/* 
 * Copyright 2008 JRimum Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Created at: 08/05/2008 - 00:10:01
 *
 * ================================================================================
 *
 * Direitos autorais 2008 JRimum Project
 *
 * Licenciado sob a Licença Apache, Versão 2.0 ("LICENÇA"); você não pode 
 * usar esse arquivo exceto em conformidade com a esta LICENÇA. Você pode obter uma 
 * cópia desta LICENÇA em http://www.apache.org/licenses/LICENSE-2.0 A menos que 
 * haja exigência legal ou acordo por escrito, a distribuição de software sob esta 
 * LICENÇA se dará “COMO ESTÁ”, SEM GARANTIAS OU CONDIÇÕES DE QUALQUER TIPO, sejam 
 * expressas ou tácitas. Veja a LICENÇA para a redação específica a reger permissões 
 * e limitações sob esta LICENÇA.
 * 
 * Criado em: 08/05/2008 - 00:10:01
 * 
 */

package br.com.nordestefomento.jrimum.bopepo.view;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.nordestefomento.jrimum.ACurbitaObject;
import br.com.nordestefomento.jrimum.JRimumException;
import br.com.nordestefomento.jrimum.bopepo.Boleto;

import com.lowagie.text.DocumentException;

/**
 * 
 * <p>
 * Agrupa as formas de visualização de um boleto.
 * </p>
 * 
 * <p>
 * EXEMPLO de formas de visualização:
 * <ul>
 * <li>PDF</li>
 * <li>Stream</li>
 * <li>Array de Bytes</li>
 * </ul>
 * </p>
 * 
 * @author <a href="http://gilmatryx.googlepages.com/">Gilmar P.S.L.</a>
 * @author Misael
 * @author Romulo
 * @author Samuel
 * 
 * @since 0.2
 * 
 * @version 0.2
 */

public class BoletoViewer extends ACurbitaObject {

	//TODO Teste no teste unitário.
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p> Engine responsável pela visualização em formato <em>PDF</em>.
	 */
	private ViewerPDF viewerPDF;

	/**
	 * @param boleto
	 * @throws DocumentException
	 * @throws IOException
	 */
	public BoletoViewer(Boleto boleto) throws JRimumException {
		super();

			initViewerPDF(null, null, boleto);			
	}
	
	/**
	 * @param boleto
	 * @param templatePathName
	 * @throws JRimumException
	 */
	public BoletoViewer(Boleto boleto, String templatePathName) throws JRimumException {
		super();

		initViewerPDF(templatePathName, null, boleto);
	}
	
	/**
	 * @param boleto
	 * @param template
	 * @throws JRimumException
	 */
	public BoletoViewer(Boleto boleto, File template) throws JRimumException {
		super();

		initViewerPDF(null, template, boleto);
	}

	/**
	 *<p> Para uso interno do componente </p> 
	 */
	protected BoletoViewer() {
		
		this.viewerPDF = new ViewerPDF();
	}

	/**
	 * <p>
	 * Agrupo vários boletos em um único pdf.
	 * </p>
	 * 
	 * @param pathName
	 *            Caminho no qual será gerado o pdf
	 * @param boletos
	 *            Boletos a serem agrupados
	 * @return Arquivo pdf
	 * @throws JRimumException
	 * 
	 * @since 0.2
	 */
	public static File groupInOnePDF(String pathName, List<Boleto> boletos)
			throws JRimumException {

		File group = null;
		
		if (validatePathName(pathName))
			if (validateBoletosList(boletos))
				group = groupInOnePDF(pathName, boletos, new BoletoViewer());

		return group;
	}

	public static File groupInOnePDF(String destPathName, List<Boleto> boletos,
			String templatePathName) throws JRimumException {

		File group = null;

		if (validatePathName(destPathName))
			if (validateBoletosList(boletos))
				if (validatePathName(templatePathName))
					group = groupInOnePDF(destPathName, boletos, new BoletoViewer().setTemplate(templatePathName));
					
		return group;
	}
	
	public static File groupInOnePDF(String destPathName, List<Boleto> boletos,
			File templateFile) throws JRimumException {

		File group = null;

		if (validatePathName(destPathName))
			if (validateBoletosList(boletos))
				if (validateFile(templateFile, "template"))
					group = groupInOnePDF(destPathName, boletos, new BoletoViewer().setTemplate(templateFile));
					
		return group;
	}

	/**
	 * <p>
	 * Gera vários arquivos pdf, cada qual com o seu boleto.
	 * </p>
	 * 
	 * @param path Caminho no qual será gerados os arquivos
	 * @param extensao TODO
	 * @param boletos Boletos a partir dos quais serão gerados os arquivos
	 * @return Vários arquivos pdf
	 * @throws JRimumException
	 * 
	 * @since 0.2
	 */

	public static List<File> onePerPDF(String path, String extensao, List<Boleto> boletos)
			throws JRimumException {

		if (isNotNull(path, "path") && isNotNull(boletos, "boletos")) {

			if(StringUtils.isNotBlank(path)){
				if(!boletos.isEmpty()){
					
						try {
							
							ViewerPDF.onePerPDF(path, extensao, boletos);
							
						} catch (IOException e) {
							
							JRimumException jrie =  new JRimumException("Erro durante a geração dos arquivos",e); 
							
							log.error("Erro durante a geração de PDFs para boletos", e);
							
							throw jrie;
							
						} catch (DocumentException e) {
							
							JRimumException jrie =  new JRimumException("Erro durante a geração dos arquivos",e); 
							
							log.error("Erro durante a geração de PDFs para boletos", e);
							
							throw jrie;
						}
					
				}else
					throw new JRimumException(new IllegalArgumentException("A Lista de boletos está vazia!"));
			}else
				throw new JRimumException(new IllegalArgumentException("Path(Diretório) destinado a geração dos arquivos não contém informação!"));
		}
		return null;
	}

	
	public File getTemplate() {

		return viewerPDF.getTemplate();
	}

	
	/**
	 * <p>
	 * Define o template que será utilizado para construir o boleto.
	 * </p>
	 * 
	 * @param template
	 * 
	 * @since 0.2
	 */
		
	public BoletoViewer setTemplate(File template) {

		if(isNotNull(template))
			this.viewerPDF.setTemplate(template);
		else
			throw new JRimumException(new IllegalArgumentException("Arquivo de template inválido!"));
		
		return this;
	}

	
	/**
	 * <p>
	 * @see BoletoViewer#setTemplate(File)
	 * </p>
	 * 
	 * @param pathName
	 * 
	 * @since 0.2
	 */
		
	public BoletoViewer setTemplate(String pathName) {
		
		if(isNotBlank(pathName))
			viewerPDF.setTemplate(pathName);
		else
			throw new JRimumException(new IllegalArgumentException("Caminho do template inválido!"));
		
		return this;
	}
	
	/**
	 * <p>
	 * Caso algum template tenha sido utilizado, este método define que após sua
	 * execução o boleto será consturído com o template padrão.
	 * </p>
	 * 
	 * @since 0.2
	 */

	public BoletoViewer removeTemplate() {

		final String PADRAO = null;

		if(isNotNull(viewerPDF))
			viewerPDF.setTemplate(PADRAO);

		return this;
	}

	/**
	 * <p>
	 * Retorna o boleto em um arquivo pdf.
	 * </p>
	 * 
	 * @param pathName Caminho onde será criado o arquivo pdf
	 * @return File
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws DocumentException
	 * 
	 * @since 0.2
	 */

	public File getPdfAsFile(String pathName) throws IllegalArgumentException,
			IOException, DocumentException {

		if (log.isDebugEnabled())
			log.debug("documento instance : " + viewerPDF);

		return viewerPDF.getFile(pathName);
	}

	/**
	 * <p>
	 * Retorna o boleto em uma stream.
	 * </p>
	 * 
	 * @return ByteArrayOutputStream
	 * @throws IOException
	 * @throws DocumentException
	 * 
	 * @since 0.1
	 */

	public ByteArrayOutputStream getPdfAsStream() throws IOException,
			DocumentException {

		if (log.isDebugEnabled())
			log.debug("documento instance : " + viewerPDF);

		return viewerPDF.getStream();

	}

	/**
	 * <p>
	 * Retorna o boleto em um array de bytes.
	 * </p>
	 * 
	 * @return byte[]
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 * 
	 * @since 0.1
	 */

	public byte[] getPdfAsByteArray() throws IOException, DocumentException {

		if (log.isDebugEnabled())
			log.debug("documento instance : " + viewerPDF);

		return viewerPDF.getBytes();
	}

	/**
	 * @return the boleto
	 * 
	 * @since 0.2
	 */
	public Boleto getBoleto() {
		
		return viewerPDF.getBoleto();
	}

	/**
	 * @param boleto
	 *            the boleto to set
	 * 
	 * @since 0.2
	 */
	public BoletoViewer setBoleto(Boleto boleto) {
		
		if(isNotNull(boleto, "boleto"))
			updateViewerPDF(boleto);
		
		return this;
	}

	private static boolean validatePathName(String pathName){
		
		boolean ok = false;
		
		if (isNotNull(pathName, "pathName")){
			if(StringUtils.isNotBlank(pathName))
				ok = true;
			else
				throw new JRimumException(new IllegalArgumentException("Path(Diretório) destinado a geração do(s) arquivo(s) não contém informação!"));
		}
		
		return ok;
	}
	
	private static boolean validateFile(File file, String name){
		
		boolean ok = false;
		
		if (isNotNull(file))
				ok = true;
		else
			throw new JRimumException(new IllegalArgumentException("File(Arquivo) destinado a geração do(s) documento(s) ["+name+"] nulo!"));
		
		return ok;
	}
	
	private static boolean validateBoletosList(List<Boleto> boletos){
		
		boolean ok = false;
		
		if (isNotNull(boletos, "boletos")) {
			if(!boletos.isEmpty())
				ok = true;					
			else
				throw new JRimumException(new IllegalArgumentException("A Lista de boletos está vazia!"));
		}
		
		return ok;
	}
	
	private static File groupInOnePDF(String pathName, List<Boleto> boletos, BoletoViewer boletoViewer){
		
		try {
			
			return ViewerPDF.groupInOnePDF(pathName, boletos, boletoViewer);

		} catch (IOException e) {

			JRimumException jrie = new JRimumException(
					"Erro durante a geração do arquivo", e);

			log.error("Erro no agrupamento de boletos", e);

			throw jrie;

		} catch (DocumentException e) {

			JRimumException jrie = new JRimumException(
					"Erro durante a geração do arquivo", e);

			log.error("Erro no agrupamento de boletos", e);

			throw jrie;
		}
		
	}

	private void initViewerPDF(String templatePathName, File template, Boleto boleto) {
		
		try {

		if(isNotNull(boleto, "boleto"))
			this.viewerPDF = new ViewerPDF(boleto);
			
			/*
			 *O arquivo tem prioridade 
			 */
			if(isNotBlank(templatePathName) && isNotNull(template))
				setTemplate(template);
			else{
				
				if(isNotBlank(templatePathName))
					setTemplate(templatePathName);
				
				if(isNotNull(template))
					setTemplate(template);
			}
				
		} catch (Exception e) {
			throw new JRimumException(e);
		}
	}
	
	
	/**
	 * <p>
	 * Atualiza o objeto BoletoViewer mantendo as "invariantes".
	 * </p>
	 * 
	 * @param boleto
	 * 
	 * @since 
	 */
		
	private void updateViewerPDF(Boleto boleto) {

		if(isNotNull(this.viewerPDF))
			this.viewerPDF = new ViewerPDF(boleto,this.viewerPDF.getTemplate());
		else
			this.viewerPDF = new ViewerPDF(boleto);		
	}
}
