package reports;
/**
 * This class describe the income report
 * @param id - the store id that we shows the income report
 * @param income - the income amount.
 * @param q - the quarter of the report
 * @param year - the year of the report
 *
 */
public class IncomeRep {
	private String id;
	private Float income;
	private String q;
	private String year;


	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	public IncomeRep(String id, Float income, String q, String year) {
		super();
		this.id = id;
		this.income = income;
		this.q = q;
		this.year = year;
	}
	public IncomeRep(String id, String q, String year) {
		super();
		this.id = id;
		
		this.q = q;
		this.year = year;
		this.income = ReportController.getStoreIncome(id, q, Integer.parseInt(year));
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Float getIncome() {
		return income;
	}
	public void setIncome(Float income) {
		this.income = income;
	}
}
