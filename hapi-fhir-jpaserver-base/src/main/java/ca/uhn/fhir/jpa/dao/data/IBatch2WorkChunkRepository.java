/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public interface IBatch2WorkChunkRepository extends JpaRepository<Batch2WorkChunkEntity, String>, IHapiFhirJpaRepository {

	@Query("SELECT e FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId ORDER BY e.mySequence ASC")
	List<Batch2WorkChunkEntity> fetchChunks(Pageable thePageRequest, @Param("instanceId") String theInstanceId);

	@Query("SELECT DISTINCT e.myStatus from Batch2WorkChunkEntity e where e.myInstanceId = :instanceId AND e.myTargetStepId = :stepId")
	List<WorkChunkStatusEnum> getDistinctStatusesForStep(@Param("instanceId") String theInstanceId, @Param("stepId") String theStepId);

	/**
	 * Deprecated, use {@link ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository#fetchChunksForStep(String, String)}
	 */
	@Deprecated
	@Query("SELECT e FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId AND e.myTargetStepId = :targetStepId ORDER BY e.mySequence ASC")
	List<Batch2WorkChunkEntity> fetchChunksForStep(Pageable thePageRequest, @Param("instanceId") String theInstanceId, @Param("targetStepId") String theTargetStepId);

	@Query("SELECT e FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId AND e.myTargetStepId = :targetStepId ORDER BY e.mySequence ASC")
	Stream<Batch2WorkChunkEntity> fetchChunksForStep(@Param("instanceId") String theInstanceId, @Param("targetStepId") String theTargetStepId);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myEndTime = :et, " +
		"e.myRecordsProcessed = :rp, e.myErrorCount = e.myErrorCount + :errorRetries, e.mySerializedData = null " +
		"WHERE e.myId = :id")
	void updateChunkStatusAndClearDataForEndSuccess(@Param("id") String theChunkId, @Param("et") Date theEndTime,
		@Param("rp") int theRecordsProcessed, @Param("errorRetries") int theErrorRetries, @Param("status") WorkChunkStatusEnum theInProgress);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myEndTime = :et, e.mySerializedData = null, e.myErrorMessage = :em WHERE e.myId IN(:ids)")
	void updateAllChunksForInstanceStatusClearDataAndSetError(@Param("ids") List<String> theChunkIds, @Param("et") Date theEndTime, @Param("status") WorkChunkStatusEnum theInProgress, @Param("em") String theError);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myEndTime = :et, e.myErrorMessage = :em, e.myErrorCount = e.myErrorCount + 1 WHERE e.myId = :id")
	int updateChunkStatusAndIncrementErrorCountForEndError(@Param("id") String theChunkId, @Param("et") Date theEndTime, @Param("em") String theErrorMessage, @Param("status") WorkChunkStatusEnum theInProgress);

	@Modifying
	@Query("UPDATE Batch2WorkChunkEntity e SET e.myStatus = :status, e.myStartTime = :st WHERE e.myId = :id AND e.myStatus IN :startStatuses")
	int updateChunkStatusForStart(@Param("id") String theChunkId, @Param("st") Date theStartedTime, @Param("status") WorkChunkStatusEnum theInProgress, @Param("startStatuses") Collection<WorkChunkStatusEnum> theStartStatuses);

	@Modifying
	@Query("DELETE FROM Batch2WorkChunkEntity e WHERE e.myInstanceId = :instanceId")
	void deleteAllForInstance(@Param("instanceId") String theInstanceId);

	@Query("SELECT e.myId from Batch2WorkChunkEntity e where e.myInstanceId = :instanceId AND e.myTargetStepId = :stepId AND e.myStatus = :status")
	List<String> fetchAllChunkIdsForStepWithStatus(@Param("instanceId")String theInstanceId, @Param("stepId")String theStepId, @Param("status") WorkChunkStatusEnum theStatus);
}
