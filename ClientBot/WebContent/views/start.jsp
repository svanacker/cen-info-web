<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:f="http://java.sun.com/jsf/core">
<body>
<ui:composition template="template.jsp">
	<ui:define name="title">Simulateur</ui:define>
	<ui:define name="body">
		<f:view>
			<rich:tabPanel>

				<rich:tab label="Gameboard">
					<ui:include src="gameboard.jsp" />
				</rich:tab>

				<rich:tab label="Com">
					<ui:include src="com.jsp" />
				</rich:tab>
				
				<rich:tab label="Attributes">
					<ui:include src="attributes.jsp" />
				</rich:tab>
				
				<rich:tab label="Parameters">
					<ui:include src="parameters.jsp" />
				</rich:tab>
				
				<!-- 
				<rich:tab label="Log">
					<ui:include src="log.jsp" />
				</rich:tab>
				<rich:tab label="ComSignatures">
					<ui:include src="comSignatures.jsp" />
				</rich:tab>		
				 -->
				<rich:tab label="Services">
					<ui:include src="services.jsp" />
				</rich:tab>
			</rich:tabPanel>

			<a4j:form rendered="#{not empty SwingView}">
				<a4j:commandButton action="#{SwingView.show}"
					value="Démarre l'interface Swing" />
			</a4j:form>
		</f:view>
	</ui:define>
</ui:composition>
</body>
</html>
