<ui:composition xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:c="http://java.sun.com/jstl/core">

	<f:subview id="launchSubview">
		<h:form>
			<h:panelGrid columns="2">
				<h:column>
					<h:outputText value="Vis à Bille :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/arrow_up_green.png"
						value="Up" action="#{Container2009View.up}" />
					<a4j:commandButton image="../images/arrows/arrow_down_green.png"
						value="Down" action="#{Container2009View.down}" />
					<a4j:commandButton image="../images/arrows/arrow_up_green_16.png"
						value="MiniUp" action="#{Container2009View.miniUp}" />
					<a4j:commandButton image="../images/arrows/arrow_down_green_16.png"
						value="MiniDown" action="#{Container2009View.miniDown}" />
					<a4j:commandButton image="../images/arrows/navigate_down2.png"
						value="GotoBottom" action="#{Container2009View.gotoBottom}" />
				</h:column>
				
				<h:column>
					<h:outputText value="Déplacement latéral Pince :" />
				</h:column>
				<h:column>				
					<a4j:commandButton image="../images/arrows/arrow_left_green.png"
						value="Left" action="#{Container2009View.left}" />
					<a4j:commandButton image="../images/arrows/nav_plain_green.png"
						value="Left" action="#{Container2009View.middle}" />
					<a4j:commandButton image="../images/arrows/arrow_right_green.png"
						value="Right" action="#{Container2009View.right}" />
				</h:column>
								
				<h:column>
					<h:outputText value="Pince :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/lock/lock_open.png"
						value="Open" action="#{Container2009View.openPlier}" />
					<a4j:commandButton image="../images/lock/lock.png"
						value="Close" action="#{Container2009View.closePlier}" />
				</h:column>
				
				<h:column>
					<h:outputText value="Déplacement Pince à linteaux :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/nav_undo_blue.png"
						value="Open" action="#{Container2009View.deployLintel}" />
					<a4j:commandButton image="../images/arrows/nav_redo_blue.png"
						value="Close" action="#{Container2009View.undeployLintel}" />
				</h:column>
				
				<h:column>
					<h:outputText value="Fermeture Pince à linteaux :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/media_play.png"
						value="Open" action="#{Container2009View.closeLintel}" />
					<a4j:commandButton image="../images/arrows/media_stop.png"
						value="Close" action="#{Container2009View.openLintel}" />
				</h:column>
				
				<h:column>
					<h:outputText value="Ramasser un palet :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/2009/1column.png"
						value="takeColumn" action="#{Container2009View.takeColumn}" />
				</h:column>
				
				<h:column>
					<h:outputText value="Avancer :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/arrows/nav_left_red.png"
						value="takeColumn" action="#{Container2009View.goForward}" />
				</h:column>
				
				<h:column>
					<h:outputText value="Preparer l'arrivee sur la colline :" />
				</h:column>
				<h:column>
					<a4j:commandButton
						value="prepareToBuild" action="#{Container2009View.prepareToBuild}" />
				</h:column>
				
				<h:column>
					<h:outputText value="2 x 2 colonnes, 1 linteau :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/2009/4columnsLintel.png"
						value="buildFirstLintelType1" action="#{Container2009View.buildFirstColumnsAndLintelType1}" />
				</h:column>
				
				<h:column>
					<h:outputText value="2x1 colonnes, 1 linteau, 2x1 colonnes : " />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/2009/2_2columnsLintel.png"
						value="buildFirstLintelType2" action="#{Container2009View.buildFirstColumnsAndLintelType2}" />
				</h:column>
				
				<h:column>
					<h:outputText value="Charger 1 linteau supplementaire : " />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/2009/loadLintel.png"
						value="buildSecondLintel" action="#{Container2009View.loadSecondLintel}" />
				</h:column>

				<h:column>
					<h:outputText value="1 linteau supplementaire : " />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/2009/secondLintel.png"
						value="buildSecondLintel" action="#{Container2009View.buildSecondLintel}" />
				</h:column>
				
				
				<h:column>
					<h:outputText value="Sequence complete :" />
				</h:column>
				<h:column>
					<a4j:commandButton image="../images/2009/sequence2009.png"
						value="takeColumn" action="#{Container2009View.completeSequence}" />
				</h:column>

				<h:column>
					<h:outputText value="Motor test : " />
				</h:column>
				<h:column>
					<a4j:commandButton 
						value="motorTest" action="#{Container2009View.motorTest}" />
				</h:column>
				
			</h:panelGrid>
		</h:form>
	</f:subview>

</ui:composition>
