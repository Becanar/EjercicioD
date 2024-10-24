package org.example.ejerciciod.app;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.ejerciciod.model.Persona;

import java.util.ArrayList;
/**
 * Controlador de la vista de la tabla de personas.
 * Permite gestionar la adición de personas y la interacción con los componentes de la vista.
 */
public class tablaController {

    @FXML
    private Button btAgregar;

    @FXML
    private TableColumn<Persona,String> columnaApellidos;

    @FXML
    private TableColumn<Persona,Integer> columnaEdad;

    @FXML
    private TableColumn<Persona,String> columnaNombre;

    @FXML
    private VBox rootPane;

    @FXML
    private TableView<Persona> tablaVista;

    private ObservableList<Persona> personas = FXCollections.observableArrayList();

    private TextField txtNombre;

    private TextField txtApellidos;

    private TextField txtEdad;

    private Button btnGuardar;

    private Button btnCancelar;

    private Stage modal;
    /**
     * Inicializa las columnas de la tabla con los valores de los atributos de la clase Persona.
     * Este método se ejecuta automáticamente al cargar la vista FXML.
     */
    public void initialize() {
        columnaNombre.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getNombre()));
        columnaApellidos.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getApellidos()));
        columnaEdad.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEdad()));
    }

    /**
     * Método que se ejecuta al hacer clic en el botón para agregar una persona.
     * Verifica si los campos son válidos y, si lo son, añade la persona a la tabla.
     * @param event Evento de acción asociado al clic del botón.
     */
    @FXML
    void agregarPersona(ActionEvent event) {
        mostrarVentanaDatos((Stage) btAgregar.getScene().getWindow());
        btnGuardar.setOnAction(actionEvent -> guardar());
        btnCancelar.setOnAction(actionEvent -> cancelar());

    }

    /**
     * Muestra una alerta de error con una lista de mensajes de error.
     * @param lst Lista de errores a mostrar en la alerta.
     */
    private void mostrarAlertError(ArrayList<String> lst) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(btAgregar.getScene().getWindow());
        alert.setHeaderText(null);
        alert.setTitle("Error");
        String error="";
        for(String str:lst) {
            error+=str+"\n";
        }
        alert.setContentText(error);
        alert.showAndWait();
    }
    /**
     * Muestra una ventana de confirmación indicando que la persona fue agregada correctamente.
     */
    private void mostrarVentanaAgregado() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.initOwner(btAgregar.getScene().getWindow());
        alerta.setHeaderText(null);
        alerta.setTitle("Info");
        alerta.setContentText("Persona agregada correctamente.");
        alerta.showAndWait();
    }
    /**
     * Muestra una ventana modal para introducir los datos de una nueva persona.
     *
     * @param ventanaPrincipal Stage principal que servirá de propietario del modal.
     */
    public void mostrarVentanaDatos( Stage ventanaPrincipal) {
        modal = new Stage();
        modal.setResizable(false);
        try {
            Image img = new Image(getClass().getResource("/org/example/ejerciciod/agenda.png").toString());
            modal.getIcons().add(img);
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
        }
        modal.initOwner(ventanaPrincipal);
        modal.initModality(Modality.WINDOW_MODAL);
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.getColumnConstraints().addAll(new ColumnConstraints(75), new ColumnConstraints(200));
        Label lblNombre = new Label("Nombre");
        txtNombre = new TextField();
        gridPane.add(lblNombre, 0, 0, 1, 1);
        gridPane.add(txtNombre, 1, 0, 1, 1);
        Label lblApellidos = new Label("Apellidos");
        txtApellidos = new TextField();
        gridPane.add(lblApellidos, 0, 1, 1, 1);
        gridPane.add(txtApellidos, 1, 1, 1, 1);
        Label lblEdad = new Label("Edad");
        txtEdad = new TextField();
        txtEdad.setMaxWidth(75);
        gridPane.add(lblEdad, 0, 2, 1, 1);
        gridPane.add(txtEdad, 1, 2, 1, 1);
        btnGuardar = new Button("Guardar");
        btnCancelar = new Button("Cancelar");
        FlowPane flowPane = new FlowPane(btnGuardar, btnCancelar);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setHgap(20);
        gridPane.add(flowPane, 0, 3, 2, 1);
        Scene scene = new Scene(gridPane, 300, 150);
        modal.setScene(scene);
        modal.setResizable(false);
        modal.setTitle("Nueva Persona");
        modal.show();
    }
    /**
     * Guarda una nueva persona a la tabla si los datos son válidos.
     * Si la persona ya existe, muestra un error.
     */
    public void guardar() {
        boolean resultado = valido();
        if (resultado) {
            Persona p = new Persona(txtNombre.getText(), txtApellidos.getText(), Integer.parseInt(txtEdad.getText()));
           personas = tablaVista.getItems();
            if (personas.contains(p)) {
                ArrayList<String> lst=new ArrayList<>();
                lst.add("La persona ya existe.");
                mostrarAlertError(lst);
            } else {
                tablaVista.getItems().add(p);
                mostrarVentanaAgregado();
                modal.close();
            }
        }
    }
    /**
     * Cierra la ventana modal sin añadir ninguna persona.
     */
    public void cancelar() {
        modal.close();
    }
    /**
     * Verifica si los datos introducidos son válidos.
     * Muestra errores si los campos están vacíos o si el valor de edad no es un número.
     *
     * @return true si los datos son válidos, false en caso contrario.
     */
    private boolean valido(){
        boolean error=false;
        ArrayList<String> errores=new ArrayList<>();
        if(txtNombre.getText().equals("")){
            errores.add("El campo Nombre es obligatorio.");
            error=true;
        }
        if(txtApellidos.getText().equals("")){
            errores.add("El campo Apellidos es obligatorio.");
            error=true;
        }
        try{Integer.parseInt(txtEdad.getText());} catch (NumberFormatException e) {
            errores.add("El campo Edad debe ser numérico.");
            error=true;
        }
        if(error){
            mostrarAlertError(errores);
            return false;

        }
        return true;
    }

}




