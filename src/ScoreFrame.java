import javax.swing.*;
import java.awt.*;

public class ScoreFrame extends JFrame {
    ScoreFrame(){
        setSize(450, 450);

        String[] columnNames = {
                "id",
                "Username",
                "Fish count",
                "Time"
        };
        String [][] data = new String[Habitat.results.size()][4];

        for(int i = 0; i < Habitat.results.size(); i++){
            data[i][0] = Integer.toString(Habitat.results.get(i).getId());
            data[i][1] = Habitat.results.get(i).getUsername();
            data[i][2] = Integer.toString(Habitat.results.get(i).getFish_count());
            data[i][3] = Integer.toString(Habitat.results.get(i).getTime());
        }
        for(int i = 0; i < Habitat.results.size(); i++)
            for(int j = 0; j < 4; j++)
                System.out.println(data[i][j]);
        JTable scoreTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setPreferredSize(new Dimension(400,400));
        add(scrollPane);
        setVisible(true);
    }
}
