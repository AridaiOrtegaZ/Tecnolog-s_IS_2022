/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;
import java.util.ArrayList;
import java.util.Optional;

import modelo.Jugador;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


/**
 *
 * @author Aridai
 */
public class SeleccionJugadorDialog extends Dialog<Jugador> {

	private Button createBt, okButton;
	private ListView<String> listaJugador;
	
	private ListProperty<String> nombreJugadores = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<String>()));
	private ObservableList<Jugador> oPlayersList;
	private ListProperty<Jugador> playersList;
	
	public SeleccionJugadorDialog( ListProperty<Jugador> jugadores) {
		
		
		setTitle("Jugadores");
		setHeaderText("Seleccione un jugador");
	
		ImageView icon = new ImageView( getClass().getResource("/images/playersIcon.png").toString());
		icon.setFitWidth(48.f);
		icon.setFitHeight(48.f);
		setGraphic(icon);
		

		createBt = new Button("Crear jugador");
		listaJugador = new ListView<>();
		listaJugador.setPrefHeight(128.f);

		oPlayersList = jugadores.get();
		playersList = new SimpleListProperty<>(oPlayersList);

		for( Jugador j : playersList.get()) {
			nombreJugadores.add(j.getNombre());
		}
		
		listaJugador.itemsProperty().bind(nombreJugadores);
		
		playersList.bindBidirectional(jugadores);
		
		HBox root = new HBox(5, listaJugador, createBt);
		root.setPadding( new Insets(10) );
		root.setFillHeight(false);

		getDialogPane().setContent(root);
		
		ButtonType oButton = new ButtonType( "Aceptar", ButtonData.OK_DONE);
		ButtonType cButton = new ButtonType( "Cancelar", ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().addAll(oButton, cButton);
		
		okButton = (Button) getDialogPane().lookupButton(oButton);
		okButton.disableProperty().bind(listaJugador.getSelectionModel().selectedItemProperty().isNull());
		
		createBt.setOnAction( evt -> createJugador() );
		
		setResultConverter( bt -> {
			
			if( bt.getButtonData() == ButtonData.OK_DONE ) {
				String jName = listaJugador.getSelectionModel().getSelectedItem();
				return playersList.stream().filter( j -> j.getNombre().equals(jName)).findFirst().get();
			}
			
			return null;
	
		});
	}
	
	private void createJugador() {
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Jugador");
		dialog.setHeaderText("Crear jugador");
		
		ImageView icon = new ImageView( getClass().getResource("/images/playerIcon.png").toString());
		icon.setFitWidth(48.f);
		icon.setFitHeight(48.f);
		dialog.setGraphic(icon);
		
		Optional<String> nombreJugador = dialog.showAndWait();
		
		if( nombreJugador.isPresent() && !nombreJugador.get().isBlank() && !nombreJugador.get().isEmpty()) {
			Jugador j = new Jugador(nombreJugador.get(), 0);
			// Añadimos nuestro jugador a la lista principal
			playersList.add(j); 
			nombreJugadores.add(nombreJugador.get());
			listaJugador.getSelectionModel().clearSelection();
			listaJugador.getSelectionModel().select(nombreJugador.get());
		}
	}
}
