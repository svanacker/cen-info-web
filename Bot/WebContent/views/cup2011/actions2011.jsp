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
			<h:panelGrid columns="2">

				<h:column>
					<h:outputText value="Ascenseur :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/arrow_down_green.png"
						value="liftDown" action="#{Actions2011View.liftDown}" />
					<a4j:commandButton image="../images/arrows/arrow_up_green.png"
						value="liftUp" action="#{Actions2011View.liftUp}" />
					<a4j:commandButton image="../images/arrows/navigate_down2.png"
						value="liftToBottom" action="#{Actions2011View.liftToBottom}" />
				</h:column>

				<h:column>
					<h:outputText value="Pince :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/lock/lock.png" value="Up"
						action="#{Actions2011View.closePlier}" />
					<a4j:commandButton image="../images/lock/lock_open.png"
						value="Down" action="#{Actions2011View.openPlier}" />
				</h:column>

				<h:column>
					<h:outputText value="Déplacements :" />
				</h:column>
				<h:column>
					<a4j:commandButton value="Forward"
						action="#{Actions2011View.forward}" />
					<a4j:commandButton value="Backward"
						action="#{Actions2011View.backward}" />
					<a4j:commandButton value="Left" action="#{Actions2011View.left}" />
					<a4j:commandButton value="Right" action="#{Actions2011View.right}" />
					<p />
					<a4j:commandButton value="Left start" action="#{Actions2011View.leftStart}" />
					<a4j:commandButton value="Right start" action="#{Actions2011View.rightStart}" />
				</h:column>
			</h:panelGrid>
		</h:form>
	</f:subview>

</ui:composition>
