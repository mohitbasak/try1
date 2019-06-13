package com.mohit.dao;

import java.io.CharConversionException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mohit.dto.BaseDto;
import com.mohit.entities.BaseDo;
import com.mohit.exceptions.ExecutionFault;
import com.mohit.exceptions.InvalidInputFault;
import com.mohit.exceptions.MessageUIDto;
import com.mohit.exceptions.NoResultFault;
import com.mohit.exceptions.NonUniqueRecordFault;
import com.mohit.exceptions.RecordExistFault;
import com.mohit.util.EnOperation;
import com.mohit.util.EntityManager;
import com.mohit.util.ServicesUtil;

@Repository
@Transactional
public abstract class BaseDao<E extends BaseDo, D extends BaseDto> {

	protected final boolean isNotQuery = false;
	protected final boolean isQuery = true;

	private final String recordExist = "Record already exist ";
	private final String noRecordFound = "No record found: "; // NOT USED

	private EntityManager entityManager;

	public BaseDao(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	public void create(D dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		persist(importDto(EnOperation.CREATE, dto));
	}

	public D getByKeys(D dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		return exportDto(getByKeysForFK(dto));
	}

	public E getByKeysForFK(D dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		return find(importDto(EnOperation.RETRIEVE, dto));
	}

	public void update(D dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		getByKeysForFK(dto);
		merge(importDto(EnOperation.UPDATE, dto));
	}

	public void delete(D dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		remove(getByKeysForFK(dto));
	}

	protected void persist(E pojo) throws ExecutionFault {
		try {
			getEntityManager().getSession().persist(pojo);
		} catch (Exception e) {
			MessageUIDto faultInfo = new MessageUIDto();
			faultInfo.setMessage(e.getMessage());
			String message = "Create of " + pojo.getClass().getSimpleName() + " with keys " + pojo.getPrimaryKey()
					+ " failed!";
			throw new ExecutionFault(message, faultInfo, e);
		}
	}

	@SuppressWarnings("unchecked")
	protected E find(E pojo) throws ExecutionFault, NoResultFault {
		E result = null;
		try {
			result = (E) getEntityManager().getSession().find(pojo.getClass(), pojo.getPrimaryKey());
		} catch (Exception e) {
			if (e instanceof CharConversionException) {
				MessageUIDto faultInfo = new MessageUIDto();
				faultInfo.setMessage(e.getMessage());
				String message = "Retrieve of " + pojo.getClass().getSimpleName() + " by keys " + pojo.getPrimaryKey()
						+ " failed due to invalid UTF-8 data";
				throw new ExecutionFault(message, faultInfo, e);
			} else {
				MessageUIDto faultInfo = new MessageUIDto();
				faultInfo.setMessage(e.getMessage());
				String message = "Retrieve of " + pojo.getClass().getSimpleName() + " by keys " + pojo.getPrimaryKey()
						+ " failed!";
				throw new ExecutionFault(message, faultInfo, e);
			}
		}
		if (result == null) {
			throw new NoResultFault(noRecordFound + pojo.getClass().getSimpleName() + "#" + pojo.getPrimaryKey());
		}
		return result;
	}

	protected void merge(E pojo) throws ExecutionFault {
		try {
			getEntityManager().getSession().clear();
			getEntityManager().getSession().update(pojo);
		} catch (Exception e) {
			MessageUIDto faultInfo = new MessageUIDto();
			faultInfo.setMessage(e.getMessage());
			String message = "Update of " + pojo.getClass().getSimpleName() + " having keys " + pojo.getPrimaryKey()
					+ " failed!";
			throw new ExecutionFault(message, faultInfo, e);
		}
	}

	protected void remove(E pojo) throws ExecutionFault {
		try {
			getEntityManager().getSession().remove(pojo);
		} catch (Exception e) {
			MessageUIDto faultInfo = new MessageUIDto();
			faultInfo.setMessage(e.getMessage());
			String message = "Delete of " + pojo.getClass().getSimpleName() + " having keys " + pojo.getPrimaryKey()
					+ " failed!";
			throw new ExecutionFault(message, faultInfo, e);
		}
	}

	private E importDto(EnOperation operation, D fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		if (fromDto != null) {
			fromDto.validate(operation);
			return importDto(fromDto);
		}
		throw new InvalidInputFault("Empty DTO passed");
	}

	protected abstract E importDto(D fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault;

	protected abstract D exportDto(E entity);

	protected List<D> exportDtoList(Collection<E> listDo) {
		List<D> returnDtos = null;
		if (!ServicesUtil.isEmpty(listDo)) {
			returnDtos = new ArrayList<D>(listDo.size());
			for (Iterator<E> iterator = listDo.iterator(); iterator.hasNext();) {
				returnDtos.add(exportDto(iterator.next()));
			}
		}
		return returnDtos;
	}

	protected void entityExist(D dto) throws ExecutionFault, RecordExistFault, InvalidInputFault {
		try {// Report entity exist
			getByKeys(dto);
			throw new RecordExistFault(recordExist, buildRecordExistFault(dto));
		} catch (NoResultFault e) {
		}
	}

	protected final EntityManager getEntityManager() {
		return entityManager;
	}

	public final void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	protected Object getSingleResult(String queryName, Query query, Object... parameters)
			throws NoResultFault, NonUniqueRecordFault {
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			throw new NoResultFault(ServicesUtil.buildNoRecordMessage(queryName, parameters));
		} catch (NonUniqueResultException e) {
			throw new NonUniqueRecordFault("Failed due to corrupt data, please contact db admin",
					buildNonUniqueRecordFault(queryName, parameters));
		}
	}

	protected List<?> getResultList(String queryName, Query query, Object... parameters) throws NoResultFault {
		List<?> returnList = query.getResultList();
		if (ServicesUtil.isEmpty(returnList)) {
			throw new NoResultFault(ServicesUtil.buildNoRecordMessage(queryName, parameters));
		}
		return returnList;
	}

	protected String uuidGen(BaseDto dto, Class<? extends BaseDo> classDo) throws ExecutionFault {
		return UUID.randomUUID().toString();
	}

	private MessageUIDto buildRecordExistFault(BaseDto mmwDto) {
		StringBuffer sb = new StringBuffer(recordExist);
		if (mmwDto != null) {
			sb.append(mmwDto.toString());
		}
		MessageUIDto messageUIDto = new MessageUIDto();
		messageUIDto.setMessage(sb.toString());
		return messageUIDto;
	}

	private MessageUIDto buildNonUniqueRecordFault(String queryName, Object... parameters) {
		StringBuffer sb = new StringBuffer("Non Unique Record found for query: ");
		sb.append(queryName);
		if (!ServicesUtil.isEmpty(parameters)) {
			sb.append(" for params:");
			sb.append(ServicesUtil.getCSV(parameters));
		}
		MessageUIDto messageUIDto = new MessageUIDto();
		messageUIDto.setMessage(sb.toString());
		return messageUIDto;
	}

	@SuppressWarnings("unchecked")
	public List<D> getAllResults(String doName, Object... parameters) throws NoResultFault {
		String queryName = "SELECT p FROM " + doName + " p";
		Query query = getEntityManager().createQuery(queryName);
		List<E> returnList = query.getResultList();
		if (ServicesUtil.isEmpty(returnList)) {
			throw new NoResultFault(ServicesUtil.buildNoRecordMessage(queryName, parameters));
		}
		return exportDtoList(returnList);
	}

	@SuppressWarnings("unchecked")
	public List<String> getResultOfColumn(String jpql) throws NoResultFault {
		Query query = getEntityManager().createQuery(jpql);
		query.setFlushMode(FlushModeType.COMMIT);
		List<Object> objectList = query.getResultList();
		if (ServicesUtil.isEmpty(objectList)) {
			throw new NoResultFault(ServicesUtil.buildNoRecordMessage(jpql));
		} else {
			List<String> stringList = new ArrayList<String>(objectList.size());
			if (!ServicesUtil.isEmpty(objectList))
				if (objectList.get(0) instanceof Object) {
					for (Object objs : objectList) {
						String strs = new String();
						strs = objs == null ? "" : objs.toString();
						stringList.add(strs);
					}
				}
			return stringList;
		}
	}
}
