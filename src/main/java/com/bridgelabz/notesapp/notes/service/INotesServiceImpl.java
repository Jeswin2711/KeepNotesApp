package com.bridgelabz.notesapp.notes.service;

import com.bridgelabz.notesapp.exception.CustomException;
import com.bridgelabz.notesapp.notes.dto.NotesDto;
import com.bridgelabz.notesapp.notes.model.Notes;
import com.bridgelabz.notesapp.notes.repository.NotesRepository;
import com.bridgelabz.notesapp.notes.service.interfaces.INotesService;
import com.bridgelabz.notesapp.user.repository.UserRepository;
import com.bridgelabz.notesapp.utility.Response;
import com.bridgelabz.notesapp.utility.security.JwtService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class INotesServiceImpl implements INotesService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private HttpServletRequest httpServlet;

    @Autowired
    private JwtService jwtService;


    public Response addNoteById(int id , NotesDto notesDto) throws CustomException {
        Notes notes = mapper.map(notesDto , Notes.class);
        String authorizationHeader = httpServlet.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String userName = jwtService.extractUsername(jwt);
        if(userRepository.findById(id).get().getUserName().equals(userName)) {
            userRepository.findById(id).get().getNotes().add(notes);
            notesRepository.save(notes);
        }
        else
        {
            throw new CustomException("ID and Token Not Matching");
        }
        return new Response("Note Added Successfully",HttpStatus.OK);
    }

    public Response getNoteById(int user_id ,int note_id) throws CustomException {
        boolean flag = false;
        if(userRepository.findById(user_id).isPresent())
        {
            List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();
            for(Notes note : userNotes)
            {
                if (note.getId() == note_id)
                {
                    flag = true;
                }
            }
            if (!false)
            {
                System.out.println("Note ID Present");
            }
            else
            {
                throw new CustomException("NOTE ID Not Found");
            }
        }
        else
        {
            throw new CustomException("Invalid User Id");
        }
        NotesDto notesDto = mapper.map(notesRepository.getById(note_id) , NotesDto.class);
        return new Response("Note ID : "+ note_id , notesDto);
    }


    public Response getNotesById(int user_id) throws CustomException {
        List<Notes> userNotes;
        if(userRepository.findById(user_id).isPresent())
        {
            userNotes = notesRepository.findUnArchived();
        }
        else
        {
            throw new CustomException("Invalid User Id");
        }
        return new Response("User Id : "+ user_id , userNotes);
    }


    public Response deleteNoteById(int user_id , int note_id ) throws CustomException {
        if(userRepository.findById(user_id).isPresent())
        {
            List<Notes> userNotes = userRepository.findById(user_id).get().getNotes();
            for(Notes note : userNotes)
            {
                if (note.getId() == note_id)
                {
                    notesRepository.deleteById(note_id);
                }
            }
        }
        else
        {
            throw new CustomException("Invalid User Id");
        }
        return new Response("Deleted Successfully Note ID : "+ note_id , HttpStatus.OK);
    }


    public Response archieveNote(String username , int note_id)
    {
        String authorizationHeader = httpServlet.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String userName = jwtService.extractUsername(jwt);
        if(userRepository.findByUserName(username).get().getUserName().equals(userName)) {
            notesRepository.deleteById(note_id);
        }
        return new Response("Note Archieved Successfully",HttpStatus.OK);
    }

    public Response unArchieveNote(String username , int note_id)
    {
        String authorizationHeader = httpServlet.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String userName = jwtService.extractUsername(jwt);
        if(userRepository.findByUserName(username).get().getUserName().equals(userName)) {
            notesRepository.findById(note_id).get().setArchieved(false);
            notesRepository.save(notesRepository.findById(note_id).get());
        }
        return new Response("Notes Un-Archieved Successfully",HttpStatus.OK);
    }

}
