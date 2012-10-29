<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:view>
		<h2><h:outputText value="Signatures" /></h2>
		<h:form id="outDataSignaturesForm">
			<p />
			<a4j:commandButton id="signaturesPopulateButton" value="Populate"
				action="#{ComView.populateSignatures}"
				reRender="outDataSignaturesForm,outDataSignatures"
				rendered="#{empty ComView.dataSignatures}" />
			<p />
		</h:form>
		<h:outputText id="outDataSignatures" escape="false"
			value="#{ComView.dataSignatures}" />

		<h:form id="outDataSignaturesCompareForm">
			<p><h:inputTextarea cols="80" rows="10"
				value="#{ComView.inputDataSignatures}" /><br />
			<a4j:commandButton id="signaturesCompareButton" value="Check"
				action="#{ComView.compareSignatures}"
				reRender="outDataSignaturesComparison" /></p>
		</h:form>
		<h:outputText id="outDataSignaturesComparison" escape="false"
			value="#{ComView.dataSignaturesComparison}" />
	</f:view>

</ui:composition>
