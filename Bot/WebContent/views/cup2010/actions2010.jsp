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
					<h:outputText value="Hissage du robot :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/arrow_up_green.png"
						value="Up" action="#{Actions2010View.robotUp}" />
					<a4j:commandButton image="../images/arrows/arrow_down_green.png"
						value="Down" action="#{Actions2010View.robotDown}" />
				</h:column>

				<h:column>
					<h:outputText value="Ramassage maïs gauche :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/arrow_down_green.png"
						value="Down" action="#{Actions2010View.cornLeftDown}" />
					<a4j:commandButton value="Collect"
						action="#{Actions2010View.cornLeftCollect}" />
				</h:column>

				<h:column>
					<h:outputText value="Ramassage maïs droite :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/arrow_down_green.png"
						value="Down" action="#{Actions2010View.cornRightDown}" />
					<a4j:commandButton value="Collect"
						action="#{Actions2010View.cornRightCollect}" />
				</h:column>

				<h:column>
					<h:outputText value="Ramassage tomates :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/arrow_up_red.png"
						value="On" action="#{Actions2010View.tomatoOn}" />
					<a4j:commandButton image="../images/arrows/arrow_down_red.png"
						value="Off" action="#{Actions2010View.tomatoOff}" />
				</h:column>

				<h:column>
					<h:outputText value="Ramassage oranges :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/nav_up_right_yellow.png"
						value="Orange" action="#{Actions2010View.pickOrange}" />
				</h:column>

				<h:column>
					<h:outputText value="Dépose :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/navigate_down2.png"
						value="Release" action="#{Actions2010View.releaseObjects}" />
				</h:column>
			</h:panelGrid>
		</h:form>
	</f:subview>

</ui:composition>
