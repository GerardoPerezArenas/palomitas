Dim oWord,oWordDoc
Dim bTablesProcessed()

Sub verDocumento(documento,soloLectura,dominioFinal)
On Error Resume Next

	Set oWord = CreateObject("Word.Application")
	If Err Then
	    mensajeError "ErrorMessages.Word.NotOpen"
	    oWord.Quit
	    Exit Sub
	End If

    Set oWordDoc = oWord.Documents.Open(documento)
    If Err Then
        mensajeError "ErrorMessages.Document.NotOpen"
        oWord.Quit
        Exit Sub
    End If

	'template = "https://" & window.location.host & "/jsp/editor/agora.dot"
    template = dominioFinal & "/jsp/editor/agora.dot"
	'oWordDoc.AttachedTemplate = template 
	If Not soloLectura Then
		oWordDoc.cargarCombo "",""
		If Err Then
	        mensajeError "ErrorMessages.Document.NotTemplate"
	        oWord.Quit
	        Exit Sub
	    End If

        oWord.Visible = True

		URL = dominioFinal & "/sge/DocumentosExpediente.do" & _
		      ";jsessionid=" & document.forms(0).sessionID.value & _ 
		      "?opcion=grabarDocumento&" & _
              "codProcedimiento=" & document.forms(0).codProcedimiento.value & "&" & _
              "ejercicio=" & document.forms(0).ejercicio.value & "&" & _
              "numeroExpediente=" & document.forms(0).numeroExpediente.value & "&" & _
              "codTramite=" & document.forms(0).codTramite.value & "&" & _
              "ocurrenciaTramite=" & document.forms(0).ocurrenciaTramite.value & "&" & _
              "codDocumento=" & document.forms(0).codDocumento.value & "&" & _
              "codUsuario=" & document.forms(0).codUsuario.value & "&" & _
              "numeroDocumento=" & document.forms(0).numeroDocumento.value
		If isNull(oWordDoc.Variables("url")) then
			oWordDoc.Variables.Add "url"
		End If
		If isNull(oWordDoc.Variables("nombre")) then
			oWordDoc.Variables.Add "nombre"
		End If
		oWordDoc.Variables("url").Value = URL
		oWordDoc.Variables("nombre").Value = document.forms(0).nombreDocumento.value
	End IF
	
	' ABRE EL WORD EN PRIMER PLANO
	oWord.WindowState = 2
    oWordDoc.ActiveWindow.WindowState = 1
	
	Set oWord = Nothing
End Sub

Sub verDocumentoRelacion(documento,soloLectura,dominioFinal)
On Error Resume Next

	Set oWord = CreateObject("Word.Application")
	If Err Then
	    mensajeError "ErrorMessages.Word.NotOpen"
	    oWord.Quit
	    Exit Sub
	End If

    Set oWordDoc = oWord.Documents.Open(documento)
    If Err Then
        mensajeError "ErrorMessages.Document.NotOpen"
        oWord.Quit
        Exit Sub
    End If
	'template = "https://" & window.location.host & "/jsp/editor/agora.dot"
    template = dominioFinal & "/jsp/editor/agora.dot"
    'MsgBox template
	'oWordDoc.AttachedTemplate = template
	IF Not soloLectura Then
		oWordDoc.cargarCombo "",""
        If Err Then
            mensajeError "ErrorMessages.Document.NotTemplate"
            oWord.Quit
            Exit Sub
        End If

        oWord.Visible = True

		URL = dominioFinal & "/sge/DocumentosRelacionExpedientes.do" & _
		      ";jsessionid=" & document.forms(0).sessionID.value & _
		      "?opcion=grabarDocumento&" & _
              "codProcedimiento=" & document.forms(0).codProcedimiento.value & "&" & _
              "ejercicio=" & document.forms(0).ejercicio.value & "&" & _
              "numeroExpediente=" & document.forms(0).numeroExpediente.value & "&" & _
              "codTramite=" & document.forms(0).codTramite.value & "&" & _
              "ocurrenciaTramite=" & document.forms(0).ocurrenciaTramite.value & "&" & _
              "codDocumento=" & document.forms(0).codDocumento.value & "&" & _
              "codUsuario=" & document.forms(0).codUsuario.value & "&" & _
              "numeroDocumento=" & document.forms(0).numeroDocumento.value
		If isNull(oWordDoc.Variables("url")) then
			oWordDoc.Variables.Add "url"
		End If
		If isNull(oWordDoc.Variables("nombre")) then
			oWordDoc.Variables.Add "nombre"
		End If
		oWordDoc.Variables("url").Value = URL
		oWordDoc.Variables("nombre").Value = document.forms(0).nombreDocumento.value
	End IF

	' ABRE EL WORD EN PRIMER PLANO
	oWord.WindowState = 2
    oWordDoc.ActiveWindow.WindowState = 1

	Set oWord = Nothing
End Sub

Sub verDocumentoDesdeTramitacion(documento)
	Set oWord = CreateObject("Word.Application")
    oWord.Visible = True
	Set oWordDoc = oWord.Documents.Open(documento)
	oWordDoc.Attachedtemplate = ""
	
	' ABRE EL WORD EN PRIMER PLANO
	oWord.WindowState = 2
    oWordDoc.ActiveWindow.WindowState = 1
	
	Set oWord = Nothing
End Sub


Sub ejecutaPlantilla(plantilla,URL,etiquetas,valores,dominioFinal)
On Error Resume Next
	
	Set oWord = CreateObject("Word.Application")
	If Err Then
	    oWord.Quit
	    mensajeError "ErrorMessages.Word.NotOpen"
	    Exit Sub
	End If

	If IsEmpty(etiquetas) Or IsNull(etiquetas) Then
		etiquetas = ""
		valores = ""
	End If
	If Not IsNull(plantilla) And Not IsEmpty(plantilla) And Not(plantilla="") Then
		Set oWordDoc = oWord.Documents.Open(plantilla)
	    If Err Then
	        mensajeError "ErrorMessages.Document.NotOpen"
	        oWord.Quit
	    Exit Sub
	End If
	Else 
		Set oWordDoc = oWord.Documents.Add
	End If
	'template = "https://" & window.location.host & "/jsp/editor/agora.dot"

	template = dominioFinal & "/jsp/editor/agora.dot"
	'MsgBox "attachedTemplate antes asignacion " & template

	If Not IsNull(plantilla) And Not IsEmpty(plantilla) And Not(plantilla="") Then
		
	Else
		oWordDoc.AttachedTemplate = template
	End If
	
	oWordDoc.cargarCombo CStr(etiquetas),CStr(valores)
	If Err Then
	    mensajeError "ErrorMessages.Document.NotTemplate"
	    oWord.Quit
	    Exit Sub
	End If

    oWord.Visible = True

	If isNull(oWordDoc.Variables("url")) then
			oWordDoc.Variables.Add "url"
	End If
	If isNull(oWordDoc.Variables("nombre")) then
			oWordDoc.Variables.Add "nombre"
	End If

	oWordDoc.Variables("url").Value = URL
	oWordDoc.Variables("nombre").Value = document.forms(0).nombreDocumento.value
	
	' ABRE EL WORD EN PRIMER PLANO
	oWord.WindowState = 2
    	oWordDoc.ActiveWindow.WindowState = 1
	
	Set oWord = Nothing
	Set oWordDoc = Nothing
End Sub

Sub ejecutaPlantillaDesdeInformes(plantilla,URL,etiquetas,valores, dominioFinal)
	Set oWord = CreateObject("Word.Application")
    oWord.Visible = True
	If IsEmpty(etiquetas) Or IsNull(etiquetas) Then
		etiquetas = ""
		valores = ""
	End If
	If Not IsNull(plantilla) And Not IsEmpty(plantilla) And Not(plantilla="") Then
		Set oWordDoc = oWord.Documents.Open(plantilla)		
	Else 
		Set oWordDoc = oWord.Documents.Add
	End If
	'template = "https://" & window.location.host & "/jsp/editor/agora.dot"
	template = dominioFinal & "/jsp/editor/agora.dot"
        'MsgBox "desdeInformes antes asignacion " & template

	If Not IsNull(plantilla) And Not IsEmpty(plantilla) And Not(plantilla="") Then
			
	Else 
		oWordDoc.AttachedTemplate = template
	End If
	
	oWordDoc.cargarCombo CStr(etiquetas),CStr(valores)
	
	If isNull(oWordDoc.Variables("url")) then
			oWordDoc.Variables.Add "url"
	End If
	If isNull(oWordDoc.Variables("nombre")) then
			oWordDoc.Variables.Add "nombre"
	End If

	oWordDoc.Variables("url").Value = URL
	oWordDoc.Variables("nombre").Value = document.forms(0).nombreDocumento.value
	
	Set oWord = Nothing
	Set oWordDoc = Nothing
End Sub


Sub ejecutaDocumento(plantilla,datos,fichero,dominioFinal)
On Error Resume Next
    m_sWordTemplate = plantilla
    m_sSource = datos
    m_bFile = fichero

    Set m_oXML = CreateObject("Msxml2.DOMDocument")
    m_oXML.async = False
    
	'Cargamos los datos XML...
    If m_bFile Then
		bLoaded = m_oXML.load(m_sSource)
    Else
	    bLoaded = m_oXML.loadXML(m_sSource)
	End If
    
	'Si hemos cargado correctamente el XML
    If bLoaded Then
		Set oXMLDoc = m_oXML.documentElement
		If oXMLDoc.hasChildNodes Then
		   Set oWord = CreateObject("Word.Application")
		   If Err Then
                mensajeError "ErrorMessages.Word.NotOpen"
                oWord.Quit
		        Exit Sub
		   End If

		   Set oWordDoc = oWord.Documents.Open(m_sWordTemplate)
		   If Err Then
		        mensajeError "ErrorMessages.Document.NotOpen"
	            oWord.Quit
	            Exit Sub
	       End If

		   
		   'oWordDoc.Attachedtemplate = template
		   'If InStr(1, oWordDoc.AttachedTemplate, "agora.dot", VBTextCompare)=0 Then
		   		'template = "https://" & window.location.host & "/jsp/editor/agora.dot"
                template = dominioFinal & "/jsp/editor/agora.dot"
                'MsgBox template
			'	oWordDoc.AttachedTemplate = template
		   'End If

		   oWordDoc.cargarCombo "",""
		    If Err Then
	            mensajeError "ErrorMessages.Document.NotTemplate"
	            oWord.Quit
	            Exit Sub
	        End If
		   URL = dominioFinal & "/sge/DocumentosExpediente.do" & _
		      ";jsessionid=" & document.forms(0).sessionID.value & _ 
		      "?opcion=grabarDocumento&" & _
              "codProcedimiento=" & document.forms(0).codProcedimiento.value & "&" & _
              "ejercicio=" & document.forms(0).ejercicio.value & "&" & _
              "numeroExpediente=" & document.forms(0).numeroExpediente.value & "&" & _
              "codTramite=" & document.forms(0).codTramite.value & "&" & _
              "ocurrenciaTramite=" & document.forms(0).ocurrenciaTramite.value & "&" & _
              "codDocumento=" & document.forms(0).codDocumento.value & "&" & _
              "codUsuario=" & document.forms(0).codUsuario.value & "&" & _
              "numeroDocumento=" & document.forms(0).numeroDocumento.value

		   If isNull(oWordDoc.Variables("url")) then
			oWordDoc.Variables.Add "url"
		   End If
		   If isNull(oWordDoc.Variables("nombre")) then
			oWordDoc.Variables.Add "nombre"
		   End If
	       oWordDoc.Variables("url").Value = URL
   		   oWordDoc.Variables("nombre").Value = document.forms(0).nombreDocumento.value
		   oWord.visible = True
           oWord.windowState = 0
  		   oWord.Activate
           oWord.ActiveDocument.Content.Select
		   oWord.Selection.Copy
		   Call procesaNodo(oXMLDoc.ChildNodes(0))
		   For i= 1 To oXMLDoc.ChildNodes.length-1
		   MsgBox  "333232323"
		       oWord.ActiveDocument.Sections.Add.Range.Paste
			   Call procesaNodo(oXMLDoc.ChildNodes(i))
		   Next
		   For Each cam In oWord.ActiveDocument.Range.Fields
		   	 cam.Update
		   Next
		End If
	End If
	oWordDoc.ActiveWindow.View.Type = 3
	oWordDoc.ActiveWindow.View.SeekView = wdSeekMainDocument
	
	' ABRE EL WORD EN PRIMER PLANO
	oWord.WindowState = 2
    oWordDoc.ActiveWindow.WindowState = 1

    Set m_oXML = Nothing
End Sub

Sub ejecutaDocumentoJustificante(plantilla,datos,fichero,dominioFinal)
On Error Resume Next

    m_sWordTemplate = plantilla
    m_sSource = datos
    m_bFile = fichero

    Set m_oXML = CreateObject("Msxml2.DOMDocument")
    m_oXML.async = False
    
	'Cargamos los datos XML...
    If m_bFile Then
		bLoaded = m_oXML.load(m_sSource)
    Else
	    bLoaded = m_oXML.loadXML(m_sSource)
	End If
    
	'Si hemos cargado correctamente el XML
    If bLoaded Then
		Set oXMLDoc = m_oXML.documentElement
		If oXMLDoc.hasChildNodes Then
		   Set oWord = CreateObject("Word.Application")
		   If Err Then
                mensajeError "ErrorMessages.Word.NotOpen"
                oWord.Quit
		        Exit Sub
		   End If

		   Set oWordDoc = oWord.Documents.Open(m_sWordTemplate)
		   If Err Then
		        mensajeError "ErrorMessages.Document.NotOpen"
	            oWord.Quit
	            Exit Sub
	       End If

		   
		   'oWordDoc.Attachedtemplate = template
		   'If InStr(1, oWordDoc.AttachedTemplate, "agora.dot", VBTextCompare)=0 Then
		   		'template = "http://" & window.location.host & "/jsp/editor/agora.dot"
                template = dominioFinal & "/jsp/editor/agora.dot"
			'	oWordDoc.AttachedTemplate = template
		   'End If

		   oWordDoc.cargarCombo "",""
		    If Err Then
	            mensajeError "ErrorMessages.Document.NotTemplate"
	            oWord.Quit
	            Exit Sub
	        End If
		   URL = ""

		   If isNull(oWordDoc.Variables("url")) then
			oWordDoc.Variables.Add "url"
		   End If
		   If isNull(oWordDoc.Variables("nombre")) then
			oWordDoc.Variables.Add "nombre"
		   End If
	       oWordDoc.Variables("url").Value = URL
   		   oWordDoc.Variables("nombre").Value = document.forms(0).nombreDocumento.value
		   oWord.visible = True
           oWord.windowState = 0
  		   oWord.Activate
           oWord.ActiveDocument.Content.Select
		   oWord.Selection.Copy
		   Call procesaNodo(oXMLDoc.ChildNodes(0))
		   For i= 1 To oXMLDoc.ChildNodes.length-1
		       oWord.ActiveDocument.Sections.Add.Range.Paste
			   Call procesaNodo(oXMLDoc.ChildNodes(i))
		   Next
		   For Each cam In oWord.ActiveDocument.Range.Fields
		   	 cam.Update
		   Next

                   oWordDoc.AttachedTemplate = ""
		End If
	End If
	oWordDoc.ActiveWindow.View.Type = 3
	oWordDoc.ActiveWindow.View.SeekView = wdSeekMainDocument
	
	' ABRE EL WORD EN PRIMER PLANO
	oWord.WindowState = 2
    oWordDoc.ActiveWindow.WindowState = 1

    Set m_oXML = Nothing
End Sub





Sub ejecutaDocumentoRelacion(plantilla,datos,fichero,dominioFinal)
On Error Resume Next

    m_sWordTemplate = plantilla
    m_sSource = datos
    m_bFile = fichero

    Set m_oXML = CreateObject("Msxml2.DOMDocument")
    m_oXML.async = False

	'Cargamos los datos XML...
    If m_bFile Then
		bLoaded = m_oXML.load(m_sSource)
    Else
	    bLoaded = m_oXML.loadXML(m_sSource)
	End If

	'Si hemos cargado correctamente el XML
    If bLoaded Then
		Set oXMLDoc = m_oXML.documentElement
		If oXMLDoc.hasChildNodes Then
		   Set oWord = CreateObject("Word.Application")
		   If Err Then
                mensajeError "ErrorMessages.Word.NotOpen"
                oWord.Quit
		        Exit Sub
		   End If
		   
		   Set oWordDoc = oWord.Documents.Open(m_sWordTemplate)
           If Err Then
		        mensajeError "ErrorMessages.Document.NotOpen"
	            oWord.Quit
	            Exit Sub
	       End If

		   'oWordDoc.Attachedtemplate = template
		   'If InStr(1, oWordDoc.AttachedTemplate, "agora.dot", VBTextCompare)=0 Then
		   		'template = "https://" & window.location.host & "/jsp/editor/agora.dot"
                template = dominioFinal & "/jsp/editor/agora.dot"
                'MsgBox template
			'	oWordDoc.AttachedTemplate = template
		   'End If

            oWordDoc.cargarCombo "",""
            If Err Then
                mensajeError "ErrorMessages.Document.NotTemplate"
	            oWord.Quit
	            Exit Sub
	        End If

		   URL = dominioFinal & "/sge/DocumentosRelacionExpedientes.do" & _
		      ";jsessionid=" & document.forms(0).sessionID.value & _
		      "?opcion=grabarDocumento&" & _
              "codProcedimiento=" & document.forms(0).codProcedimiento.value & "&" & _
              "ejercicio=" & document.forms(0).ejercicio.value & "&" & _
              "numeroExpediente=" & document.forms(0).numeroExpediente.value & "&" & _
              "codTramite=" & document.forms(0).codTramite.value & "&" & _
              "ocurrenciaTramite=" & document.forms(0).ocurrenciaTramite.value & "&" & _
              "codDocumento=" & document.forms(0).codDocumento.value & "&" & _
              "codUsuario=" & document.forms(0).codUsuario.value & "&" & _
              "numeroDocumento=" & document.forms(0).numeroDocumento.value

		   If isNull(oWordDoc.Variables("url")) then
			oWordDoc.Variables.Add "url"
		   End If
		   If isNull(oWordDoc.Variables("nombre")) then
			oWordDoc.Variables.Add "nombre"
		   End If
	       oWordDoc.Variables("url").Value = URL
   		   oWordDoc.Variables("nombre").Value = document.forms(0).nombreDocumento.value
		   oWord.visible = True
           oWord.windowState = 0
  		   oWord.Activate
           oWord.ActiveDocument.Content.Select
		   oWord.Selection.Copy
		   Call procesaNodo(oXMLDoc.ChildNodes(0))
		   For i= 1 To oXMLDoc.ChildNodes.length-1
		       oWord.ActiveDocument.Sections.Add.Range.Paste
			   Call procesaNodo(oXMLDoc.ChildNodes(i))
		   Next
		   For Each cam In oWord.ActiveDocument.Range.Fields
		   	 cam.Update
		   Next
		End If
	End If
	oWordDoc.ActiveWindow.View.Type = 3
	oWordDoc.ActiveWindow.View.SeekView = wdSeekMainDocument

	' ABRE EL WORD EN PRIMER PLANO
	oWord.WindowState = 2
    oWordDoc.ActiveWindow.WindowState = 1

    Set m_oXML = Nothing
End Sub

Sub procesaNodo(xmlNode)
    ReDim bTablesProcessed(oWordDoc.Tables.Count)
	If xmlNode.baseName="EXPEDIENTE_COL" Then
		Call replaceFieldsNoPorInteresado(xmlNode,oWordDoc.mailMerge.fields)
		Call replaceFieldsNoPorInteresado(xmlNode,oWordDoc.sections.last.Headers.item(1).range.fields)
		Call replaceFieldsNoPorInteresado(xmlNode,oWordDoc.sections.last.Footers.item(1).range.fields)
	Else
		Call replaceFields(xmlNode,oWordDoc.mailMerge.fields)
		Call replaceFields(xmlNode,oWordDoc.sections.last.Headers.item(1).range.fields)
		Call replaceFields(xmlNode,oWordDoc.sections.last.Footers.item(1).range.fields)
	End If
End Sub


Sub replaceFieldsNoPorInteresado(oXMLDocNode,campos)
	Dim oXMLFieldNode
	For Each oWordField In campos
		If oWordField.Type = 59 Then
        	oWordField.Select
        	Set oWordFieldRange = oWord.Selection.Range
        	bTableField = False
        	nTable = -1
        	For Each oWordTable In oWordDoc.Tables
            	nTable = nTable + 1
            	If oWordFieldRange.InRange(oWordTable.Range) Then
            		bTableField = True
                	Exit For
           		End If
    		Next

			sFieldSplit2 = Split(oWordField.Code, " ")
        	sFieldSplit = Split(sFieldSplit2(2), "_")

			Set oXMLFieldNode = oXMLDocNode
        	Set oXMLParentNode = Nothing
        	For n = LBound(sFieldSplit) To UBound(sFieldSplit)
				Set oXMLFieldNode = oXMLFieldNode.selectSingleNode(UCase(sFieldSplit(n)))
				If oXMLFieldNode Is Nothing Then
					Exit For
				End If
                If oXMLParentNode Is Nothing Then
					Set oXMLNextNode = oXMLFieldNode.nextSibling
                	If Not oXMLNextNode Is Nothing Then
						If oXMLNextNode.baseName = oXMLFieldNode.baseName Then
                    		Set oXMLParentNode = oXMLFieldNode
                        	nParentIndex = n
                        	Exit For
                  		End If
             		End If
				End If
        	Next

            On Error Resume Next
			nombreBaseHijo = oXMLFieldNode.baseName
			

			If bTableField And Not oXMLParentNode Is Nothing Then
				nRow = 0
            	nCol = 0
            	Do While Not oXMLParentNode Is Nothing
            		Set oXMLFieldNode = oXMLParentNode
                	For n = nParentIndex + 1 To UBound(sFieldSplit)
                		Set oXMLFieldNode = oXMLFieldNode.selectSingleNode(UCase(sFieldSplit(n)))
                    	If oXMLFieldNode Is Nothing Then
							Exit For
						End If
                	Next
                	If oXMLFieldNode Is Nothing Then
                		oWordFieldRange.text = ""
                	Else
                    	If nRow = 0 Then
                    		bRowColFound = False
							If oXMLFieldNode.childNodes.length > 0 Then
							  If Not oXMLFieldNode.childNodes(0).Text = "" Then
	                        	For nRow = 1 To oWordTable.Rows.Count
	                        		For nCol = 1 To oWordTable.Columns.Count
	                            		If oWordFieldRange.InRange(oWordTable.Cell(nRow, nCol).Range) Then
	                                		bRowColFound = True
	                                    	Exit For
	                              		End If
	                          		Next
	                            	If bRowColFound Then
										Exit For
									End If
	                       		Next
								If oXMLFieldNode.childNodes.length > 0 Then
	                        		oWordFieldRange.Text = oXMLFieldNode.childNodes(0).Text
								Else
									oWordFieldRange.Text = ""
								End If
							  End If
							End If
						Else
                        	nRow = nRow + 1
							If oXMLFieldNode.childNodes.length > 0 Then
							  If Not oXMLFieldNode.childNodes(0).Text = "" Then
	                        	If Not bTablesProcessed(nTable) Then
	                        		If nRow <= oWordTable.Rows.Count Then
	                            		oWordTable.Rows.Add oWordTable.Rows.Item(nRow)
	                           		Else
	                            		oWordTable.Rows.Add
	                          		End If
	                      		End If
								If oXMLFieldNode.childNodes.length > 0 Then
	                        		oWordTable.Cell(nRow, nCol).Range.Text = oXMLFieldNode.childNodes(0).Text
								Else
									oWordTable.Cell(nRow, nCol).Range.Text = ""
								End If
							  End If
							End If

                  		End If
                    	Set oXMLParentNode = oXMLParentNode.nextSibling
            		End If
       			Loop
            	bTablesProcessed(nTable) = True
     		Else
				If oXMLFieldNode Is Nothing Then
					oWordFieldRange.Text = ""
           		Else
					If oXMLParentNode Is Nothing Then
						If oXMLFieldNode.childNodes.length > 0 Then
							oWordFieldRange.Text = oXMLFieldNode.childNodes(0).Text
						Else
							oWordFieldRange.Text = ""
						End If
					Else
						etiqCambiada = 0

						Do While Not oXMLParentNode Is Nothing
						
		            		Set oXMLFieldNode = oXMLParentNode
		                	For n = nParentIndex + 1 To UBound(sFieldSplit)
		                		Set oXMLFieldNode = oXMLFieldNode.selectSingleNode(UCase(sFieldSplit(n)))
		                    	If oXMLFieldNode Is Nothing Then
									Exit For
								End If
		                	Next
		                	If oXMLFieldNode Is Nothing Then
		                		oWordFieldRange.Text = ""
		                	Else
								If oXMLFieldNode.childNodes.length > 0 Then
									pos = InStr(oWordFieldRange.Text,"EXPEDIENTE_")
									If(pos>0) Then
										oWordFieldRange.Text = oXMLFieldNode.childNodes(0).Text
									Else
                                        If oWordFieldRange.Text<>"" Then
										oWordFieldRange.Text = oWordFieldRange.Text & ";" & oXMLFieldNode.childNodes(0).Text
                                        Else
                                            oWordFieldRange.Text = oXMLFieldNode.childNodes(0).Text
                                        End If
									End If
									etiqCambiada = 1
								Else
									If etiqCambiada = 0 Then
										oWordFieldRange.Text = ""
									End If

								End If

		            		End If
                                             on error resume next
			                    Set oXMLParentNode = oXMLParentNode.nextSibling		            		
		       			Loop
					End If
            	End If
      		End If
		End If
 	Next
End Sub


Sub replaceFields(oXMLDocNode,campos)

	Dim oXMLFieldNode
	For Each oWordField In campos
		If oWordField.Type = 59 Then
        	oWordField.Select
        	Set oWordFieldRange = oWord.Selection.Range
        	bTableField = False
        	nTable = -1
        	For Each oWordTable In oWordDoc.Tables
            	nTable = nTable + 1
            	If oWordFieldRange.InRange(oWordTable.Range) Then
            		bTableField = True
                	Exit For
           		End If
    		Next


			sFieldSplit2 = Split(oWordField.Code, " ")
        	sFieldSplit = Split(sFieldSplit2(2), "_")

			Set oXMLFieldNode = oXMLDocNode
        	Set oXMLParentNode = Nothing
        	For n = LBound(sFieldSplit) To UBound(sFieldSplit)
				Set oXMLFieldNode = oXMLFieldNode.selectSingleNode(UCase(sFieldSplit(n)))
				If oXMLFieldNode Is Nothing Then
					Exit For
				End If
                If oXMLParentNode Is Nothing Then
					Set oXMLNextNode = oXMLFieldNode.nextSibling
                	If Not oXMLNextNode Is Nothing Then
						If oXMLNextNode.baseName = oXMLFieldNode.baseName Then
                    		Set oXMLParentNode = oXMLFieldNode
                        	nParentIndex = n
                        	Exit For
                  		End If
             		End If
				End If
        	Next

			'alert "Nodo "&oXMLFieldNode.baseName
			'if oXMLParentNode Is Nothing Then
			'	alert "Padre nothing "
			'Else
			'	alert "Padre "&oXMLParentNode.baseName
			'End If
			'alert nParentIndex

			If bTableField And Not oXMLParentNode Is Nothing Then
				nRow = 0
            	nCol = 0
            	Do While Not oXMLParentNode Is Nothing
            		Set oXMLFieldNode = oXMLParentNode
                	For n = nParentIndex + 1 To UBound(sFieldSplit)
                		Set oXMLFieldNode = oXMLFieldNode.selectSingleNode(UCase(sFieldSplit(n)))
                    	If oXMLFieldNode Is Nothing Then
							Exit For
						End If
                	Next
                	If oXMLFieldNode Is Nothing Then
                		oWordFieldRange.text = ""
                	Else
                    	If nRow = 0 Then
                    		bRowColFound = False
							If oXMLFieldNode.childNodes.length > 0 Then
							  If Not oXMLFieldNode.childNodes(0).Text = "" Then
	                        	For nRow = 1 To oWordTable.Rows.Count
	                        		For nCol = 1 To oWordTable.Columns.Count
	                            		If oWordFieldRange.InRange(oWordTable.Cell(nRow, nCol).Range) Then
	                                		bRowColFound = True
	                                    	Exit For
	                              		End If
	                          		Next
	                            	If bRowColFound Then
										Exit For
									End If
	                       		Next
								If oXMLFieldNode.childNodes.length > 0 Then
	                        		oWordFieldRange.Text = oXMLFieldNode.childNodes(0).Text
								Else
									oWordFieldRange.Text = ""
								End If
							  End If
							End If
						Else
                        	nRow = nRow + 1
							If oXMLFieldNode.childNodes.length > 0 Then
							  If Not oXMLFieldNode.childNodes(0).Text = "" Then
	                        	If Not bTablesProcessed(nTable) Then
	                        		If nRow <= oWordTable.Rows.Count Then
	                            		oWordTable.Rows.Add oWordTable.Rows.Item(nRow)
	                           		Else
	                            		oWordTable.Rows.Add
	                          		End If
	                      		End If
								If oXMLFieldNode.childNodes.length > 0 Then
	                        		oWordTable.Cell(nRow, nCol).Range.Text = oXMLFieldNode.childNodes(0).Text
								Else
									oWordTable.Cell(nRow, nCol).Range.Text = ""
								End If
							  End If
							End If

                  		End If
                    	Set oXMLParentNode = oXMLParentNode.nextSibling
            		End If
       			Loop
            	bTablesProcessed(nTable) = True
     		Else
				If oXMLFieldNode Is Nothing Then
					oWordFieldRange.Text = ""
           		Else
					If oXMLParentNode Is Nothing Then
						If oXMLFieldNode.childNodes.length > 0 Then
							oWordFieldRange.Text = oXMLFieldNode.childNodes(0).Text
						Else
							oWordFieldRange.Text = ""
						End If
					Else
						etiqCambiada = 0
						Do While Not oXMLParentNode Is Nothing
		            		Set oXMLFieldNode = oXMLParentNode
		                	For n = nParentIndex + 1 To UBound(sFieldSplit)
		                		Set oXMLFieldNode = oXMLFieldNode.selectSingleNode(UCase(sFieldSplit(n)))
		                    	If oXMLFieldNode Is Nothing Then
									Exit For
								End If
		                	Next
		                	If oXMLFieldNode Is Nothing Then
		                		oWordFieldRange.text = ""
		                	Else
								If oXMLFieldNode.childNodes.length > 0 Then
									oWordFieldRange.Text = oXMLFieldNode.childNodes(0).Text
									etiqCambiada = 1
								Else
									If etiqCambiada = 0 Then
										oWordFieldRange.Text = ""
									End If
								End If
			                    Set oXMLParentNode = oXMLParentNode.nextSibling
		            		End If
		       			Loop
					End If
            	End If
      		End If
		End If
 	Next
End Sub