function domlay(id,trigger,lax,lay,content) {
/*
 * Cross browser Layer visibility / Placement Routine
 * Done by Chris Heilmann (mail@ichwill.net)
 * Feel free to use with these lines included!
 * Created with help from Scott Andrews.
 * The marked part of the content change routine is taken
 * from a script by Reyn posted in the DHTML
 * Forum at Website Attraction and changed to work with
 * any layername. Cheers to that!
 * Welcome DOM-1, about time you got included... :)
 */
// Layer visible
    if (trigger=="1"){
        if (document.getElementById(''+id+'')!=null) 
            document.getElementById(''+id+'').style.visibility = "visible";                        
    }
    // Layer hidden
    else if (trigger=="0"){
        if (document.getElementById(''+id+'')!=null) 
            document.getElementById(''+id+'').style.visibility = "hidden";
    }
    // Set horizontal position      
    if (lax){
        if (document.getElementById(''+id+'')!=null)
            document.getElementById(''+id+'').style.left=lax+"px";
    }
    // Set vertical position
    if (lay){
        if (document.getElementById(''+id+'')!=null)
            document.getElementById(''+id+'').style.top=lay+"px";
    }
    // change content

    if (typeof content != "undefined" && (content || (content=="" && id=='resultadoBuscar'))){ 
        if (document.getElementById(''+id+'')!=null){
            var htmlFrag;
            //Thanx Reyn!
            rng = document.createRange();
            el = document.getElementById(''+id+'');
            rng.setStartBefore(el);
            htmlFrag = rng.createContextualFragment(content);
            while(el.hasChildNodes()) 
                el.removeChild(el.lastChild);
            el.appendChild(htmlFrag);
            // end of Reyn ;)
        }
    }
}

if ((typeof Range !== "undefined") && !Range.prototype.createContextualFragment)
{
	Range.prototype.createContextualFragment = function(html)
	{
		var frag = document.createDocumentFragment(), 
		div = document.createElement("div");
		frag.appendChild(div);
		div.outerHTML = html;
		return frag;
	};
}
