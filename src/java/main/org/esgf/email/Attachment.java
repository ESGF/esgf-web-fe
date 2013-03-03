package org.esgf.email;

public class Attachment {

	private String attachmentName;
	private String attachmentContent;
	
	public Attachment() {
		this.attachmentName = "wgetblank.sh";
		this.attachmentContent = "N/A";
	}
	
	public Attachment(String attachmentName,String attachmentContent) {
		this.setAttachmentName(attachmentName);
		this.setAttachmentContent(attachmentContent);
	}

	/**
	 * @return the attachmentName
	 */
	public String getAttachmentName() {
		return attachmentName;
	}

	/**
	 * @param attachmentName the attachmentName to set
	 */
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	/**
	 * @return the attachmentContent
	 */
	public String getAttachmentContent() {
		return attachmentContent;
	}

	/**
	 * @param attachmentContent the attachmentContent to set
	 */
	public void setAttachmentContent(String attachmentContent) {
		this.attachmentContent = attachmentContent;
	}
	
}
