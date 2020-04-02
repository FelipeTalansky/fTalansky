import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Instant start = Instant.now();
		Locale l = new Locale("pt","BR");
		NumberFormat nf = NumberFormat.getInstance(l);
		String r = "NM_SUBPRODUTO"+';'+"RESULTADO" + '\n';
		String r2;
		int i = 1;
		String Mercado = "DadosMercado.csv";
		File merc = new File(Mercado);
		String Operacoes = "Operacoes.csv";
		File oper = new File(Operacoes);
		try (PrintWriter writer = new PrintWriter(new File("Resultado.csv"))) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			try {
				Scanner inputO = new Scanner(oper);
				inputO.next();
				while (inputO.hasNext()) {
					String data = inputO.next();
					String[] values = data.split(";");
					long dias = 0;

					// calcula diferença entre datas
					try {
						dias = (formatter.parse(values[2]).getTime() - formatter.parse(values[1]).getTime()) / 1000 / 60
								/ 60 / 24;
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Double resultado = 0.0;
					Scanner inputM = new Scanner(merc);
					inputM.next();
					while (inputM.hasNext()) {
						String dataM = inputM.next();
						String[] valuesM = dataM.split(";");
						if (valuesM[0].equals(values[13]) && dias == Integer.parseInt(valuesM[1])) {
							resultado = Double.parseDouble(values[12].replaceAll(",", ".")) * Double.parseDouble(valuesM[2].replaceAll(",", "."));
							break;
						}
					}
					inputM.close();
					r2=nf.format(resultado).toString();
					r=r+values[9]+';'+r2+'\n';
					i++;
				}
				inputO.close();
				writer.write(r);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		Instant finish = Instant.now();
		Duration interval = Duration.between(start, finish);
		System.out.println("O tempo de processamento foi de "+interval.getSeconds()+" segundos");
	}

}
