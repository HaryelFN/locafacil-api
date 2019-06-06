package br.com.gotech.repository.aluguel;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.gotech.dto.AluguelEstatisticaMes;
import br.com.gotech.model.Aluguel;

public class AluguelRepositoryImpl implements AluguelRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<AluguelEstatisticaMes> getTotalAluguelMes() {

		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<AluguelEstatisticaMes> criteriaQuery = criteriaBuilder.createQuery(AluguelEstatisticaMes.class);

		Root<Aluguel> root = criteriaQuery.from(Aluguel.class);

		criteriaQuery.select(criteriaBuilder.construct(AluguelEstatisticaMes.class, criteriaBuilder.function("MONTH", Integer.class, root.get("dtPagamento")), criteriaBuilder.sum(root.get("total"))));

		criteriaQuery.where(criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("dtPagamento")), criteriaBuilder.function("YEAR", Integer.class,criteriaBuilder.currentDate())));

		criteriaQuery.groupBy(criteriaBuilder.function("MONTH", Integer.class, root.get("dtPagamento")));

		TypedQuery<AluguelEstatisticaMes> typedQuery = manager.createQuery(criteriaQuery);

		return typedQuery.getResultList();

	}

}
