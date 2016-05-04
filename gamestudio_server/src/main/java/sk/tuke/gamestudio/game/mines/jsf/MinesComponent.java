package sk.tuke.gamestudio.game.mines.jsf;

import controller.User;
import controller.UserController;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.mines.core.*;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import javax.ejb.EJB;
import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Date;

@FacesComponent("Mines")
public class MinesComponent extends UICommand {
    @EJB
    private ScoreService scoreService;

    public UserController getUserController() {
        return (UserController) getStateHelper().eval("userController");
    }

    public void setUserController(UserController userController) {
        getStateHelper().put("userController", userController);
    }

    private void processParamsAndHandleAction(FacesContext context) {
        Field field = (Field) getValue();

        if (field.getState() == GameState.PLAYING) {
            try {
                String row = (String) context.getExternalContext().getRequestParameterMap().get("row");
                String column = (String) context.getExternalContext().getRequestParameterMap().get("column");
                field.action(Integer.parseInt(row), Integer.parseInt(column));
                if (field.getState() == GameState.SOLVED) {
                    UserController userController = getUserController();
                    if(userController != null && userController.getLoggedUser().isLogged()) {
                        scoreService.addScore(new Score(userController.getLoggedUser().getName(), "mines", field.getScore(), new Date()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        Field field = (Field) getValue();
        processParamsAndHandleAction(context);

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("table", this);

        for (int row = 0; row < field.getRowCount(); row++) {
            writer.startElement("tr", this);
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                writer.startElement("td", this);

                if(tile.getState()!= TileState.OPEN) {
                    writer.startElement("a", this);
                    writer.writeAttribute("href", String.format("?row=%d&column=%d", row, column), null);
                }

                String image = "";
                switch (tile.getState()) {
                    case CLOSED:
                        image = "closed";
                        break;
                    case MARKED:
                        image = "marked";
                        break;
                    case OPEN:
                        if (tile instanceof Clue) {
                            image = "open" + ((Clue) tile).getValue();
                        } else {
                            image = "mine";
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected tile state " + tile.getState());
                }
                writer.startElement("img", this);
                writer.writeAttribute("src", String.format("resources/images/mines/%s.png", image), null);
                writer.endElement("img");
                if(tile.getState()!= TileState.OPEN) {
                    writer.endElement("a");
                }
                writer.endElement("td");
            }
            writer.endElement("tr");
        }
        writer.endElement("table");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
