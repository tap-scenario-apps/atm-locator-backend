package com.example.tanzu.atm.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.tanzu.atm.entity.ATM;
import com.example.tanzu.atm.entity.ATMD;
import com.example.tanzu.atm.entity.ATMDetail;
import com.example.tanzu.atm.entity.ATMNote;
import com.example.tanzu.atm.model.ATMModel;
import com.example.tanzu.atm.repository.ATMDetailRepository;
import com.example.tanzu.atm.repository.ATMNoteRepository;
import com.example.tanzu.atm.repository.ATMRepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@OpenAPIDefinition(
        info = @Info(
                title = "ATM Service",
                version = "0.0.1",
        		description = "Service for searching for ATMs within a given a geograpical coordiate and a search radius"),
        tags = @Tag(
                name = "ATM REST API",
                description = "ATM Search API"))
@RestController
@RequestMapping("atm")
@Slf4j
public class ATMResource 
{	
	private static final String DEFAULT_RADIUS = "10";
	
	protected ATMRepository atmRepo;

	protected ATMNoteRepository atmNoteRepo;
	
	protected ATMDetailRepository atmDetailRepo;
	
	@Autowired
	public void setATMRepository(ATMRepository atmRepo)
	{
		this.atmRepo = atmRepo;
	}
	
	@Autowired
	public void setATMNoteRepository(ATMNoteRepository atmNoteRepo)
	{
		this.atmNoteRepo = atmNoteRepo;
	}
	
	@Autowired
	public void setATMDetailRepository(ATMDetailRepository atmDetailRepo)
	{
		this.atmDetailRepo = atmDetailRepo;
	}
	
	@GetMapping("/locsearch")
	public Flux<ATMModel> search(@RequestParam(name="latitude") float latitude, 
			@RequestParam(name="longitude") float longitude, 
			@RequestParam(name="radius", required=false, defaultValue=DEFAULT_RADIUS) int radius)
	{
		if (!isValidCordinates(latitude, longitude))
		{
			log.error("Invalid search paramater.");
			return Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));				
		}
	
		return getATMs(atmRepo.findByLocationDistance(latitude, longitude, radius))
    	   .onErrorResume(e -> { 
    	    	log.error("Error searching for ATMs.", e);
    	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    	   });

	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ATMModel> addAtm(@RequestBody ATMModel atm)
	{
		log.info("Adding ATM with name {}", atm.name());
		
		if (!isValidCordinates(atm.latitude(), atm.longitude()))
		{
			log.error("Invalid coordinates.");
			return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));				
		}
	
		final var newATM = new ATM(atm.id(), atm.name(), atm.latitude(), atm.longitude(), atm.addr(), atm.city(), atm.state(), 
				 atm.postalCode(), atm.inDoors(), atm.branchId());
		
		return atmRepo.saveATM(newATM)
			.flatMap(savedATM -> 
			{
				Mono<ATMModel> saveDetailMono = null;
				
				if (CollectionUtils.isEmpty(atm.details()))
					saveDetailMono = Mono.just(new ATMModel(savedATM.id(), savedATM.name(), savedATM.latitude(), savedATM.longitude(), savedATM.addr(), savedATM.city(), savedATM.state(), 
							 savedATM.postalCode(), 0d, savedATM.inDoors(),  savedATM.branchId(), Collections.emptyList(),  null));
				else
				{
					final List<ATMDetail> details = new ArrayList<>();
					for (ATMDetail detail : atm.details())
						details.add(new ATMDetail(null, detail.detail(), savedATM.id()));
						
					saveDetailMono = atmDetailRepo.saveAll(details)
					  .collectList()
					  .map(savedDetails -> new ATMModel(savedATM.id(), savedATM.name(), savedATM.latitude(), savedATM.longitude(), savedATM.addr(), savedATM.city(), savedATM.state(), 
							 savedATM.postalCode(), 0d, savedATM.inDoors(),  savedATM.branchId(), savedDetails,  null));
				}
					
				return saveDetailMono.flatMap(sATM ->
				{
					if (CollectionUtils.isEmpty(atm.notes()))
						return Mono.just(new ATMModel(savedATM.id(), sATM.name(), sATM.latitude(), sATM.longitude(), sATM.addr(), sATM.city(), sATM.state(), 
								sATM.postalCode(), sATM.distance(), sATM.inDoors(),  sATM.branchId(), sATM.details(),  Collections.emptyList()));
					
					final List<ATMNote> notes = new ArrayList<>();
					for (ATMNote note : atm.notes())
						notes.add(new ATMNote(null, note.note(), savedATM.id()));
					
					return atmNoteRepo.saveAll(notes).collectList()
						.map(addedNotes -> new ATMModel(savedATM.id(), sATM.name(), sATM.latitude(), sATM.longitude(), sATM.addr(), sATM.city(), sATM.state(), 
							sATM.postalCode(), sATM.distance(), sATM.inDoors(),  sATM.branchId(), sATM.details(),  addedNotes));
				});
				
			})
    	   .onErrorResume(e -> { 
    	    	log.error("Error adding for ATM.", e);
    	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    	   });
	}	
	
	@PostMapping("/branchAssoc/{atmId}/{branchId}") 
	public Mono<ATM> associateBranchToATM(@PathVariable("atmId") Long atmId, @PathVariable("branchId") Long branchId)
	{
		// make sure the ATM exists
		return atmRepo.findById(atmId)
			.flatMap(atm -> 
			{
				if (atm.id() == null)
					return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
				
				final var upAtm = new ATM(atm.id(), atm.name(), atm.latitude(), atm.longitude(), atm.addr(), atm.city(), atm.state(), 
						atm.postalCode(), atm.inDoors(), branchId);
				
				return atmRepo.save(upAtm);
			})
    	   .onErrorResume(e -> { 
    	    	log.error("Error adding branch id {} to ATM id.", branchId, atmId, e);
    	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    	   });
	}
		
	@PostMapping("/branchDisassoc/{atmId}") 
	public Mono<ATM> disassociateBranchFromATM(@PathVariable("atmId") Long atmId)
	{
		return associateBranchToATM(atmId, null);
	}	
	
	@DeleteMapping("{id}") 
	public Mono<Void> deleteATM(@PathVariable("id") Long id)
	{
		log.info("Deleting ATM with id {}", id);
		
		return atmRepo.deleteById(id)
			.then(atmDetailRepo.deleteByAtmId(id))
			.then(atmNoteRepo.deleteByAtmId(id))
    	   .onErrorResume(e -> { 
    	    	log.error("Error deleting ATM.", e);
    	    	return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    	   });
	}	
	
	protected boolean isValidCordinates(float latitude, float longitude)
	{
		return (longitude < 180.0 && longitude > -180.0 && latitude < 90.0 && latitude > -90);
	}	
	
	protected Flux<ATMModel> getATMs(Flux<ATMD> search)
	{
		return search.collectList().flatMapMany(Flux::fromIterable).flatMap(atm -> 
		{
			return atmNoteRepo.findByAtmId(atm.id())
				.collectList()
				.switchIfEmpty(Mono.just(Collections.emptyList()))
				.map(notes -> 
				{
					return new ATMModel(atm.id(), atm.name(), atm.latitude(), atm.longitude(), atm.addr(), atm.city(), atm.state(), 
							 atm.postalCode(), atm.distance(), atm.inDoors(),  atm.branchId(), null,  notes);
				})
				.flatMap(atmN ->
				{
					return atmDetailRepo.findByAtmId(atmN.id())
						.collectList()
						.switchIfEmpty(Mono.just(Collections.emptyList()))
						.map(details -> 
						{
							return new ATMModel(atmN.id(), atmN.name(), atmN.latitude(), atmN.longitude(), atmN.addr(), atmN.city(), atmN.state(), 
									atmN.postalCode(), atmN.distance(), atmN.inDoors(),  atmN.branchId(), details,  atmN.notes());
						});
				});	
		});
	}
}
