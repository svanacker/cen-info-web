<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:subview id="actionsSubview">
		<h:form>
			<a4j:status startText="sending..." />
			<br />
			<h:panelGrid columns="4">

				<h:column>
					<h:outputText value="Left Arm :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/nav_up_left_blue.png"
						value="leftArmUp" action="#{Actions2012View.leftArmUp}" />
						<br/>
					<a4j:commandButton image="../images/arrows/nav_left_blue.png"
						value="leftArmDown" action="#{Actions2012View.leftArmDown}" />
				</h:column>

				<h:column>
					<h:outputText value="Right Arm :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/nav_up_right_blue.png" value="rightArmUp"
						action="#{Actions2012View.rightArmUp}" />
						<br/>
					<a4j:commandButton image="../images/arrows/nav_right_blue.png"
						value="rightArmDown" action="#{Actions2012View.rightArmDown}" />
				</h:column>

			</h:panelGrid>
		</h:form>
	</f:subview>

</ui:composition>
