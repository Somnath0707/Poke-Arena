package com.pokearena.service;

import com.pokearena.entity.Trainer;
import com.pokearena.model.dto.CreateTrainerRequest;
import com.pokearena.model.dto.TrainerResponseDto;
import com.pokearena.repository.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    TrainerRepository trainerRepository ;

    TrainerService(TrainerRepository trainerRepository){
        this.trainerRepository = trainerRepository;
    }


    public TrainerResponseDto createTrainer(CreateTrainerRequest request){

        Trainer trainer = new Trainer();
        trainer.setName(request.name());
        trainer.setEmail(request.email());

        trainerRepository.save(trainer);
        return new TrainerResponseDto(
                trainer.getId(),
                trainer.getName(),
                trainer.getEmail(),
                trainer.getWins(),
                trainer.getLosses()
        );
    }

    public List<TrainerResponseDto> getLeaderBoard(){
        return trainerRepository.findTop10ByOrderByWinsDesc().stream()
                .map(t -> new TrainerResponseDto(t.getId() , t.getName(),t.getEmail() , t.getWins(),t.getLosses()))
                .toList();
    }

}
