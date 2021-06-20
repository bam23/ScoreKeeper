import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;


public class AutoCompleteBox extends JComboBox {
    private static final long serialVersionUID = 8960018347298992948L;
    /* This work is hereby released into the Public Domain.
     * To view a copy of the public domain dedication, visit
     * http://creativecommons.org/licenses/publicdomain/
     */
    @SuppressWarnings("unused") //keeps reference of the autocomplete so it will be disposed when the jcombobox is disposed (necessary?)
    private AutoComplete ac;
    private String lastItem = "";
    private JComboBox box = this;
    private int length;
    
    public AutoCompleteBox(String [] items, boolean isEditable, int length) {
        super(items);
        setBackground(Color.white);
        setEditable(isEditable); //must be editable to support autocomplete
        this.isEditable = isEditable;
        this.length = length;
        if (isEditable)
            ac = new AutoComplete();
        else
            this.addKeyListener(new PopupKeyListener());
            
    }
    public AutoCompleteBox(String [] items, boolean isEditable) {
        this(items, isEditable,0);
    }
    public AutoCompleteBox(String [] items) {
        this(items, true);
    }
    public AutoCompleteBox(String [] items, int length) {
        this(items, true, length);
    }
    
    public AutoCompleteBox() {
        this(new String[] {}, true);
    }
    
    //Allows a max length to be defined 
    public AutoCompleteBox(int length){
        this(new String[]{}, true, length);
    }
    
    private class AutoComplete extends PlainDocument {
        private static final long serialVersionUID = 547466094654496408L;

        // flag to indicate if setSelectedItem has been called
        // subsequent calls to remove/insertString should be ignored
        private boolean selecting=false;
        
        //Constructor
        public AutoComplete() {
            //Registers an actionlistener for the jcombobox to call
            //a lookup function whenever the text for the combobox's
            //text has changed
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (!selecting) 
                        highlightCompletedText(0);
                }
            });
            
            //set the document to this autocomplete document
            JTextComponent editor = getTextEditor();
            editor.setDocument(this);
           
            //When the editor loses focus, check to see if
            //the selected item has changed
            editor.addFocusListener(new FocusListener(){
                @Override
                public void focusGained(FocusEvent arg0) {
                    highlightCompletedText(0);
                } //take no action

                @Override
                public void focusLost(FocusEvent event) {
                    checkForUpdate(getSelectedItem());
                }
            });
            
            editor.addKeyListener(new PopupKeyListener());
            // Handle initially selected object
            Object selected = getSelectedItem();
            if (selected!=null) setText(selected.toString());
            highlightCompletedText(0);
        }
        
        //returns the combobox's editor
        private JTextComponent getTextEditor() {
            return (JTextComponent) getEditor().getEditorComponent();
        }
        
        //appends an autocomplete suggestion onto the text the user has typed into the jcombobox
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            // return immediately when selecting an item
            if (selecting) return;
            
            if (getLength() + str.length() > length && length > 0 ){ 
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            // insert the string into the document
            //determine if we are addding text to the end of the existing text
            boolean endOfLine = offs == getLength();
            super.insertString(offs, str, a);
            
            //only look for matches when inserting at the end of the line
            if (endOfLine) {
                // lookup and select a matching item
                Object item = lookupItem(getText(0, getLength()));
                if (item != null) {
                    setSelectedItem(item);
                    setText(item.toString());
                }
                // select the completed part
                highlightCompletedText(offs+str.length());
            }
        }
        
        //sets the text of the jcombobox to text
        private void setText(String text) {
            try {
                // remove all text and insert the completed string
                super.remove(0, getLength());
                super.insertString(0, text, null);
            } catch (BadLocationException e) {
                throw new RuntimeException(e.toString());
            }
        }
        
        //highlights a portion of the jcomboboxes text in its text field
        //at the specified starting index
        private void highlightCompletedText(int start) {
            JTextComponent editor = getTextEditor();
            try {
                if (editor.getText().length() >= getLength())
                    editor.setCaretPosition(getLength());
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }    
            editor.moveCaretPosition(start);
        }
        
        //sets the selected item of the combox's model to the specified item
        private void setSelectedItem(Object item) {
            selecting = true;
            getModel().setSelectedItem(item);
            selecting = false;
        }
        
        //look for potential suggestions in the models list to populate the 
        //jcombobox's text field with
        private Object lookupItem(String pattern) {
            ComboBoxModel model = getModel();
            Object selectedItem = model.getSelectedItem();
            // only search for a different item if the currently selected does not match
            if (selectedItem != null && startsWithIgnoreCase(selectedItem.toString(), pattern)) {
                return selectedItem;
            } else {
                // iterate over all items
                for (int i=0, n=model.getSize(); i < n; i++) {
                    Object currentItem = model.getElementAt(i);
                    // current item starts with the pattern?
                    if (currentItem != null && startsWithIgnoreCase(currentItem.toString(), pattern)) {
                        return currentItem;
                    }
                }
            }
            // no item starts with the pattern => return null
            return null;
        }
        
        // checks if str1 starts with str2 - ignores case
        private boolean startsWithIgnoreCase(String str1, String str2) {
            return str1.toUpperCase().startsWith(str2.toUpperCase());
        }
    }

    /**
     * Checks to see if the specified entry is in the list of entries in the combobox's model's list
     * @param entry - the entry to locate
     * @return - true if the entry is found in the list, otherwise false
     */
    public boolean hasEntry(String entry) {
        //iterate through the entries, looking for a match
        for (int i = 0; i < getItemCount(); i++) {
            Object obj = getItemAt(i);
            if (obj != null && obj.toString().trim().equals(entry)) return true;
        }
        return false;
    }
    
    //keeps track of the selected item, calling the update function when it changes
    @Override
    public void setSelectedItem(Object item) {
        super.setSelectedItem(item);
        checkForUpdate(item);
    }
    /**
     * checks to see if the item passed in matches the current text in the field
     * @param item - the item to be checked, if null checkForUpdate() will be called with a blank string
     */

    private void checkForUpdate(Object item) {
        //if the current item is null, update the text to blank
        if (item == null) checkForUpdate("");
        //otherwise, check to see if this item matches the previous item
        else {
            String text = item.toString();
            if(!text.equals(lastItem)) {
                lastItem = text;
                update(text);
            }
        }
    }
    //called when the selected item changes
    public void update(String text) {}
    
    //Popup the jcombobox list to show highlighted 
    //completion suggestion when the user types text into the field
    private class PopupKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}

        @Override
        public void keyTyped(KeyEvent e) {
            //popup the window for all keys except ENTER
            //close the window when ENTER is pressed
            char ch = e.getKeyChar();
            boolean isEnterKey = ch == '\n';
            if(box.isDisplayable())
                box.setPopupVisible(!isEnterKey);
        }
    }
}