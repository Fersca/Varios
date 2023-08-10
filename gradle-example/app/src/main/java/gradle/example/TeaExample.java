package gradle.example;

import org.teavm.jso.dom.html.HTMLDocument;

/**
 *
 * @author Fernando.Scasserra
 */
public class TeaExample {

    public static void main(String[] args) {
        TeaExample tea = new TeaExample();
        tea.greetings();
        var document = HTMLDocument.current();
        var div = document.createElement("div");
        div.appendChild(document.createTextNode("TeaVM generated element"));
        document.getBody().appendChild(div);      
        
    }

    private void greetings() {
        //Esto aparece en la consola del browser.
        System.out.println("Hola Fer con Tea");
    }
    
}
