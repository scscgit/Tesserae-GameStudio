package sk.tuke.gamestudio.game.mines.jsf;

import sk.tuke.gamestudio.service.GameServices;
import sk.tuke.gamestudio.game.mines.core.*;

import javax.faces.component.FacesComponent;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

@FacesComponent("Mines")
public class MinesComponent extends UICommand {

    private static final String GAME = "mines";

    public GameServices getGameServices() {
        return (GameServices) getStateHelper().eval("gameServices");
    }

    public void setGameServices(GameServices gameServices) {
        getStateHelper().put("gameServices", gameServices);
    }

    private void processParamsAndHandleAction(FacesContext context) {
        Field field = (Field) getValue();
        if(field == null)
        {
	        throw new RuntimeException("Field is null");
        }

        if (field.getState() == GameState.PLAYING) {
            try
            {
                String row = (String) context.getExternalContext().getRequestParameterMap().get("row");
                String column = (String) context.getExternalContext().getRequestParameterMap().get("column");
                field.action(Integer.parseInt(row), Integer.parseInt(column));
                if (field.getState() == GameState.SOLVED)
                {
                    GameServices gameServices = getGameServices();
                    if (gameServices != null)
                    {
                        gameServices.saveScore(GAME, field.getScore());
                    }
	                else
                    {
	                    throw new RuntimeException("GameServices is null");
                    }
                }
            } catch (NumberFormatException e) {
                return;
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

                if (tile.getState() != TileState.OPEN) {
                    writer.startElement("a", this);
                    writer.writeAttribute("href", getURL(context, String.format("?row=%d&column=%d", row, column)), null);
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
                if (tile.getState() != TileState.OPEN) {
                    writer.endElement("a");
                }
                writer.endElement("td");
            }
            writer.endElement("tr");
        }
        writer.endElement("table");
    }

    private String getURL(FacesContext context, String value) {
        return context.getExternalContext().getApplicationContextPath() + context.getViewRoot().getViewId() + value;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}
